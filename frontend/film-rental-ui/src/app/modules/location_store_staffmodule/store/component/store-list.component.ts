import { formatBackendError } from '../../../../shared/error-utils';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StoreService } from '../service/store.service';

@Component({
  standalone: true,
  selector: 'app-store-list.component',
  imports: [CommonModule, FormsModule],
  templateUrl: './store-list.component.html',
  styleUrl: './store-list.component.css',
})
export class StoreListComponent {
  items: any[] = []; filteredItems: any[] = []; pagedItems: any[] = [];
  currentPage = 1; pageSize = 10; totalPages = 1; searchTerm = '';
  loading = true; error = ''; showModal = false; editItem: any = null; formData: any = {}; successMsg = '';
  modalError = '';

  // Search by Store ID
  searchStoreId: number | null = null;
  searchStoreIdResult: any = null;
  searchStoreIdError = '';
  searchStoreIdLoading = false;

  // Search staff by Store ID
  searchStaffStoreId: number | null = null;
  searchStaffResults: any[] = [];
  searchStaffError = '';
  searchStaffLoading = false;

  constructor(private svc: StoreService, private cdr: ChangeDetectorRef) {}
  ngOnInit(): void { setTimeout(() => this.load()); }

  load(): void {
    this.loading = true; this.error = '';
    this.svc.getAll().subscribe({
      next: (d: any[]) => { this.items = d; this.filteredItems = d; this.paginate(); this.loading = false; this.cdr.detectChanges(); },
      error: (e: any) => { this.error = formatBackendError(e); this.loading = false; this.cdr.detectChanges(); }
    });
  }

  fetchByStoreId(): void {
    if (!this.searchStoreId || this.searchStoreId <= 0) { this.searchStoreIdError = 'Please enter a valid Store ID (positive number).'; this.searchStoreIdResult = null; return; }
    this.searchStoreIdLoading = true; this.searchStoreIdError = ''; this.searchStoreIdResult = null;
    this.svc.getById(this.searchStoreId).subscribe({
      next: (d: any) => { this.searchStoreIdResult = d; this.searchStoreIdLoading = false; this.cdr.detectChanges(); },
      error: (e: any) => { this.searchStoreIdError = formatBackendError(e); this.searchStoreIdLoading = false; this.cdr.detectChanges(); }
    });
  }

  clearStoreIdSearch(): void { this.searchStoreId = null; this.searchStoreIdResult = null; this.searchStoreIdError = ''; }

  fetchStaffByStore(): void {
    if (!this.searchStaffStoreId || this.searchStaffStoreId <= 0) { this.searchStaffError = 'Please enter a valid Store ID (positive number).'; this.searchStaffResults = []; return; }
    this.searchStaffLoading = true; this.searchStaffError = ''; this.searchStaffResults = [];
    this.svc.getStaffByStore(this.searchStaffStoreId).subscribe({
      next: (d: any[]) => { this.searchStaffResults = d; this.searchStaffLoading = false; this.cdr.detectChanges(); },
      error: (e: any) => { this.searchStaffError = formatBackendError(e); this.searchStaffLoading = false; this.cdr.detectChanges(); }
    });
  }

  clearStaffSearch(): void { this.searchStaffStoreId = null; this.searchStaffResults = []; this.searchStaffError = ''; }

  openCreate(): void { this.editItem = null; this.formData = { managerStaffId: null, addressId: null }; this.modalError = ''; this.showModal = true; }
  openEdit(item: any): void { this.editItem = item; this.formData = { managerStaffId: item.managerStaffId, addressId: item.addressId }; this.modalError = ''; this.showModal = true; }
  closeModal(): void { this.showModal = false; this.modalError = ''; }

  validate(): boolean {
    if (!this.formData.managerStaffId || this.formData.managerStaffId <= 0) { this.modalError = 'A valid Manager Staff ID is required.'; this.cdr.detectChanges(); return false; }
    if (!this.formData.addressId || this.formData.addressId <= 0) { this.modalError = 'A valid Address ID is required.'; this.cdr.detectChanges(); return false; }
    return true;
  }

  save(): void {
    this.modalError = '';
    if (!this.validate()) return;
    const call = this.editItem ? this.svc.update(this.editItem.storeId, this.formData) : this.svc.create(this.formData);
    call.subscribe({
      next: () => { this.successMsg = `Store ${this.editItem ? 'updated' : 'created'}!`; this.closeModal(); this.load(); setTimeout(() => this.successMsg = '', 3000); },
      error: (e: any) => { this.modalError = formatBackendError(e); this.cdr.detectChanges(); }
    });
  }

  delete(item: any): void {
    if (!confirm('Delete this Store?')) return;
    this.error = '';
    this.svc.delete(item.storeId).subscribe({
      next: () => { this.successMsg = 'Store deleted!'; this.load(); setTimeout(() => this.successMsg = '', 3000); },
      error: (e: any) => { this.error = formatBackendError(e); this.cdr.detectChanges(); }
    });
  }

  search(term: string): void {
    this.searchTerm = term;
    this.filteredItems = this.items.filter(item => JSON.stringify(item).toLowerCase().includes(term.toLowerCase()));
    this.currentPage = 1; this.paginate();
  }

  paginate(): void {
    this.totalPages = Math.max(1, Math.ceil(this.filteredItems.length / this.pageSize));
    if (this.currentPage > this.totalPages) this.currentPage = this.totalPages;
    const start = (this.currentPage - 1) * this.pageSize;
    this.pagedItems = this.filteredItems.slice(start, start + this.pageSize);
  }

  goToPage(page: number): void { if (page < 1 || page > this.totalPages) return; this.currentPage = page; this.paginate(); }
  changePageSize(size: number): void { this.pageSize = size; this.currentPage = 1; this.paginate(); }

  get pageNumbers(): number[] {
    const pages: number[] = [];
    const start = Math.max(1, this.currentPage - 2);
    const end = Math.min(this.totalPages, start + 4);
    for (let i = start; i <= end; i++) pages.push(i);
    return pages;
  }
}


