import { formatBackendError } from '../../../../shared/error-utils';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CustomerService } from '../service/customer.service';

@Component({
  standalone: true,
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  imports: [CommonModule, FormsModule]
})
export class CustomerListComponent implements OnInit {

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
  editItem: any = null;
  formData: any = {};
  successMsg = '';

  constructor(private svc: CustomerService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    setTimeout(() => this.load());
  }

  load(): void {
    this.loading = true;
    this.error = '';

    this.svc.getAll().subscribe({
      next: (d: any[]) => {
        this.items = d;
        this.filteredItems = d;
        this.paginate();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        this.error = formatBackendError(e);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  openCreate(): void {
    this.editItem = null;
    this.formData = {
      firstName: '',
      lastName: '',
      email: '',
      storeId: null,
      addressId: null,
      active: true
    };
    this.error = '';
    this.showModal = true;
  }

  openEdit(item: any): void {
    this.editItem = item;
    this.formData = {
      firstName: item.firstName,
      lastName: item.lastName,
      email: item.email,
      storeId: item.storeId,
      addressId: item.addressId,
      active: item.active
    };
    this.error = '';
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.error = '';
  }

  validate(): boolean {
    if (!this.formData.firstName?.trim()) { this.error = 'First name is required.'; return false; }
    if (!this.formData.lastName?.trim()) { this.error = 'Last name is required.'; return false; }
    if (!this.formData.email?.trim()) { this.error = 'Email is required.'; return false; }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.formData.email)) {
      this.error = 'Please enter a valid email address.';
      return false;
    }

    if (!this.formData.storeId || this.formData.storeId <= 0) {
      this.error = 'A valid Store ID is required.';
      return false;
    }

    if (!this.formData.addressId || this.formData.addressId <= 0) {
      this.error = 'A valid Address ID is required.';
      return false;
    }

    return true;
  }

  save(): void {
    this.error = '';
    if (!this.validate()) return;

    const call = this.editItem
      ? this.svc.update(this.editItem.customerId, this.formData)
      : this.svc.create(this.formData);

    call.subscribe({
      next: () => {
        this.successMsg = `Customer ${this.editItem ? 'updated' : 'created'}!`;
        this.closeModal();
        this.load();
        setTimeout(() => this.successMsg = '', 3000);
      },
      error: (e: any) => {
        this.error = formatBackendError(e);
      }
    });
  }

  delete(item: any): void {
    if (!confirm('Delete this Customer?')) return;

    this.error = '';
    this.svc.delete(item.customerId).subscribe({
      next: () => {
        this.successMsg = 'Customer deleted!';
        this.load();
        setTimeout(() => this.successMsg = '', 3000);
      },
      error: (e: any) => {
        this.error = formatBackendError(e);
      }
    });
  }

  // ✅ FIXED SEARCH (API ONLY)
  search(term: string): void {
    this.searchTerm = term;

    // If empty → load all
    if (!term || term.trim() === '') {
      this.load();
      return;
    }

    // Only numeric ID allowed
    if (isNaN(Number(term))) {
      this.error = 'Please enter a valid Customer ID';
      this.filteredItems = [];
      this.pagedItems = [];
      return;
    }

    this.loading = true;
    this.error = '';

    this.svc.getById(+term).subscribe({
      next: (res: any) => {
        this.filteredItems = [res];
        this.currentPage = 1;
        this.paginate();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        this.error = formatBackendError(e);
        this.filteredItems = [];
        this.pagedItems = [];
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
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
}