import { formatBackendError } from '../../../../shared/error-utils';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RentalService } from '../service/rental.service';

@Component({
  standalone: true,
  selector: 'app-rental-list',
  templateUrl: './rental-list.component.html',
  imports: [CommonModule, FormsModule]
})
export class RentalListComponent implements OnInit {
  items: any[] = [];
  filteredItems: any[] = [];
  pagedItems: any[] = [];
  currentPage = 1;
  pageSize = 10;
  totalPages = 1;
  searchTerm = '';
  loading = true;
  error = '';
  showModal = false;
  formData: any = {};
  successMsg = '';
  customerIdInput = '';
  customerSearchError = '';
  isCustomerSearch = false;   // ← NEW FLAG

  constructor(private svc: RentalService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void { setTimeout(() => this.load()); }

  load(): void {
    this.loading = true;
    this.error = '';
    this.svc.getAll().subscribe({
      next: (data: any[]) => {
        this.items = data;
        this.filteredItems = data;
        this.isCustomerSearch = false;   // ← RESET ON NORMAL LOAD
        this.paginate();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        this.error = e.status === 0
          ? 'Cannot connect to backend. Make sure Spring Boot is running on port 8081.'
          : e.status === 401 ? 'Authentication failed. Please login again.'
          : e.status === 403 ? 'Access denied. Your role does not have permission for this endpoint.'
          : formatBackendError(e);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  openCreate(): void {
    this.formData = {
      rentalDate: new Date().toISOString().slice(0, 19),
      inventoryId: null,
      customerId: null,
      staffId: null
    };
    this.error = '';
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.error = '';
    this.formData = {};
  }

  validate(): boolean {
    if (!this.formData.rentalDate) { this.error = 'Rental date is required.'; return false; }
    if (!this.formData.inventoryId || this.formData.inventoryId <= 0) { this.error = 'A valid Inventory ID is required.'; return false; }
    if (!this.formData.customerId || this.formData.customerId <= 0) { this.error = 'A valid Customer ID is required.'; return false; }
    if (!this.formData.staffId || this.formData.staffId <= 0) { this.error = 'A valid Staff ID is required.'; return false; }
    return true;
  }

  save(): void {
    this.error = '';
    if (!this.validate()) return;
    const payload = {
      rentalDate: this.formData.rentalDate,
      inventoryId: this.formData.inventoryId,
      customerId: this.formData.customerId,
      staffId: this.formData.staffId
    };
    this.svc.create(payload).subscribe({
      next: () => {
        this.successMsg = 'Rental created!';
        this.closeModal();
        this.load();
        setTimeout(() => this.successMsg = '', 3000);
      },
      error: (e: any) => { this.error = formatBackendError(e); }
    });
  }

  returnRental(item: any): void {
    const id = item.rentalId ?? item.id;
    if (!confirm(`Mark rental #${id} as returned?`)) return;
    this.error = '';
    this.svc.returnRental(id).subscribe({
      next: () => {
        this.successMsg = `Rental #${id} returned!`;
        this.load();
        setTimeout(() => this.successMsg = '', 3000);
      },
      error: (e: any) => { this.error = formatBackendError(e); }
    });
  }

  isSimple(val: any): boolean {
    return val === null || val === undefined || typeof val !== 'object';
  }

  keys(item: any): string[] {
  return [
    'rentalId',
    'rentalDate',
    'inventoryId',
    'customerId', 
    'staffId',
    'returnDate'
  ];
}

  search(term: string): void {
    this.searchTerm = term.trim();

    if (!this.searchTerm) {
      this.filteredItems = [...this.items];
      this.currentPage = 1;
      this.paginate();
      return;
    }

    if (!isNaN(Number(this.searchTerm))) {
      const id = Number(this.searchTerm);
      this.loading = true;
      this.error = '';
      this.svc.getById(id).subscribe({
        next: (res: any) => {
          this.filteredItems = res ? [res] : [];
          this.currentPage = 1;
          this.paginate();
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: (e: any) => {
          this.error = formatBackendError(e); // BACKEND MESSAGE
          this.filteredItems = [];
          this.currentPage = 1;
          this.paginate();
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
    } else {
      const lower = this.searchTerm.toLowerCase();
      this.filteredItems = this.items.filter(item =>
        item.filmTitle?.toLowerCase().includes(lower)
      );
      this.currentPage = 1;
      this.paginate();
    }
  }

  paginate(): void {
    this.totalPages = Math.max(1, Math.ceil(this.filteredItems.length / this.pageSize));
    if (this.currentPage > this.totalPages) this.currentPage = this.totalPages;
    const start = (this.currentPage - 1) * this.pageSize;
    this.pagedItems = this.filteredItems.slice(start, start + this.pageSize);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    this.paginate();
  }

  changePageSize(size: number): void {
    this.pageSize = size;
    this.currentPage = 1;
    this.paginate();
  }

  get pageNumbers(): number[] {
    const pages: number[] = [];
    const start = Math.max(1, this.currentPage - 2);
    const end = Math.min(this.totalPages, start + 4);
    for (let i = start; i <= end; i++) pages.push(i);
    return pages;
  }

  searchByCustomer(): void {
    const id = Number(this.customerIdInput);
    if (!id || id <= 0) {
      this.customerSearchError = 'Enter a valid Customer ID.';
      return;
    }
    this.customerSearchError = '';
    this.loading = true;
    this.svc.getByCustomerId(id).subscribe({
      next: (data: any[]) => {
        this.isCustomerSearch = true;   // ← SET FLAG: show customerId column
        this.filteredItems = data;
        this.currentPage = 1;
        this.paginate();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        this.customerSearchError = formatBackendError(e);
        this.filteredItems = [];
        this.paginate();
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  clearCustomerSearch(): void {
    this.customerIdInput = '';
    this.customerSearchError = '';
    this.isCustomerSearch = false;   // ← RESET FLAG
    this.load();
  }
}