import { formatBackendError } from '../../../../shared/error-utils';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InventoryService } from '../service/inventory.service';

@Component({
  standalone: true,
  selector: 'app-inventory-list',
  templateUrl: './inventory-list.component.html',
  imports: [CommonModule, FormsModule]
})
export class InventoryListComponent implements OnInit {

  items: any[] = [];
  filteredItems: any[] = [];
  pagedItems: any[] = [];

  currentPage = 1;
  pageSize = 10;
  totalPages = 1;

  inventorySearchTerm: string = '';
  storeSearchTerm: string = '';

  loading = true;
  error = '';
  showModal = false;
  formData: any = {};
  successMsg = '';

  constructor(private svc: InventoryService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.load();
  }

  // ================= LOAD =================
  load(): void {
    this.loading = true;
    this.error = '';

    this.svc.getAll().subscribe({
      next: (data: any[]) => {
        this.items = data;
        this.filteredItems = data;
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

  // ================= CREATE =================
  openCreate(): void {
    this.formData = { filmId: null, storeId: null };
    this.error = '';
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.error = '';
  }

  save(): void {
    this.error = '';

    if (!this.formData.filmId || !this.formData.storeId) {
      this.error = 'Film ID and Store ID required';
      return;
    }

    this.svc.create(this.formData).subscribe({
      next: () => {
        this.successMsg = 'Inventory created!';
        this.closeModal();
        this.load();
        setTimeout(() => this.successMsg = '', 3000);
      },
      error: (e: any) => {
        this.error = formatBackendError(e);
      }
    });
  }

  // ================= DELETE =================
  delete(item: any): void {
    if (!confirm('Delete this Inventory item?')) return;

    this.svc.delete(item.inventoryId ?? item.id).subscribe({
      next: () => {
        this.successMsg = 'Deleted successfully';
        this.load();
        setTimeout(() => this.successMsg = '', 3000);
      },
      error: (e: any) => {
        this.error = formatBackendError(e);
      }
    });
  }

  // ================= SEARCH BY INVENTORY ID =================
  searchByInventoryId(): void {
    const id = this.inventorySearchTerm.trim();

    if (!id) {
      this.filteredItems = this.items;
      this.currentPage = 1;
      this.paginate();
      return;
    }

    if (isNaN(Number(id))) {
      this.error = 'Enter a valid Inventory ID';
      return;
    }

    this.loading = true;
    this.error = '';

    this.svc.getById(+id).subscribe({
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
        this.paginate();
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  // ================= SEARCH BY STORE ID =================
  searchByStoreId(): void {
    const id = this.storeSearchTerm.trim();

    if (!id) {
      this.filteredItems = this.items;
    } else {
      this.filteredItems = this.items.filter(item =>
        item.storeId?.toString() === id
      );
    }

    this.currentPage = 1;
    this.paginate();
  }

  // ================= TABLE HELPERS =================
  keys(item: any): string[] {
    return Object.keys(item).slice(0, 6);
  }

  isSimple(val: any): boolean {
    return val === null || val === undefined || typeof val !== 'object';
  }

  // ================= PAGINATION =================
  paginate(): void {
    this.totalPages = Math.max(1, Math.ceil(this.filteredItems.length / this.pageSize));

    if (this.currentPage > this.totalPages) {
      this.currentPage = this.totalPages;
    }

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
