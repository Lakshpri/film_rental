import { formatBackendError } from '../../../../shared/error-utils';
import { Component, OnInit, ChangeDetectorRef  } from '@angular/core';
import { StaffService } from '../service/staff.service';
import { StoreService } from '../../store/service/store.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  standalone: true,
  selector: 'app-staff-list.component',
  imports: [FormsModule,CommonModule],
  templateUrl: './staff-list.component.html',
  styleUrl: './staff-list.component.css',
})
export class StaffListComponent {
  items: any[] = []; filteredItems: any[] = []; pagedItems: any[] = [];
  currentPage = 1; pageSize = 10; totalPages = 1; searchTerm = '';
  loading = true;
  pageError  = '';   // load-level error (top of page)
  modalError = '';   // create/edit modal
  showModal = false; editItem: any = null; formData: any = {}; successMsg = '';

  // Per-row delete errors in main table
  tableDeleteErrors: { [staffId: number]: string } = {};

  // Search by Staff ID
  searchStaffId: number | null = null;
  searchStaffIdResult: any = null;
  searchStaffIdError = '';
  searchStaffIdLoading = false;
  staffIdDeleteError = '';   // delete error inside the search-by-id card

  // Search Staff by Store ID
  searchStoreId: number | null = null;
  searchStoreResults: any[] = [];
  searchStoreError = '';
  searchStoreLoading = false;
  storeDeleteErrors: { [staffId: number]: string } = {};  // per-row in store results
  storeDeleteSuccess = '';

  constructor(private svc: StaffService, private storeSvc: StoreService, private cdr: ChangeDetectorRef) {}
  ngOnInit(): void { setTimeout(() => this.load()); }

  load(): void {
    this.loading = true; this.pageError = ''; this.tableDeleteErrors = {};
    this.svc.getAll().subscribe({
      next: (d: any[]) => { this.items = d; this.filteredItems = d; this.paginate(); this.loading = false; this.cdr.detectChanges(); },
      error: (e: any) => { this.pageError = formatBackendError(e); this.loading = false; this.cdr.detectChanges(); }
    });
  }

  // ── Search by Staff ID ──────────────────────────────────────────────
  fetchByStaffId(): void {
    if (!this.searchStaffId || this.searchStaffId <= 0) {
      this.searchStaffIdError = 'Please enter a valid Staff ID (positive number).';
      this.searchStaffIdResult = null; return;
    }
    this.searchStaffIdLoading = true; this.searchStaffIdError = '';
    this.searchStaffIdResult = null; this.staffIdDeleteError = '';
    this.svc.getById(this.searchStaffId).subscribe({
      next: (d: any) => { this.searchStaffIdResult = d; this.searchStaffIdLoading = false; this.cdr.detectChanges(); },
      error: (e: any) => { this.searchStaffIdError = formatBackendError(e); this.searchStaffIdLoading = false; this.cdr.detectChanges(); }
    });
  }
  clearStaffIdSearch(): void {
    this.searchStaffId = null; this.searchStaffIdResult = null;
    this.searchStaffIdError = ''; this.staffIdDeleteError = '';
  }

  // Delete from the search-by-id card — error stays inside the card
  deleteFromCard(item: any): void {
    if (!confirm(`Delete Staff #${item.staffId} — ${item.firstName} ${item.lastName}?`)) return;
    this.staffIdDeleteError = '';
    this.svc.delete(item.staffId).subscribe({
      next: () => {
        this.successMsg = `Staff "${item.firstName} ${item.lastName}" deleted.`;
        this.clearStaffIdSearch();
        this.load();
        setTimeout(() => this.successMsg = '', 3000);
      },
      error: (e: any) => {
        this.staffIdDeleteError = formatBackendError(e);
        this.cdr.detectChanges();
      }
    });
  }

  // ── Search Staff by Store ID ────────────────────────────────────────
  fetchByStoreId(): void {
    if (!this.searchStoreId || this.searchStoreId <= 0) {
      this.searchStoreError = 'Please enter a valid Store ID (positive number).';
      this.searchStoreResults = []; return;
    }
    this.searchStoreLoading = true; this.searchStoreError = '';
    this.searchStoreResults = []; this.storeDeleteErrors = {};
    this.storeSvc.getStaffByStore(this.searchStoreId).subscribe({
      next: (d: any[]) => { this.searchStoreResults = d; this.searchStoreLoading = false; this.cdr.detectChanges(); },
      error: (e: any) => { this.searchStoreError = formatBackendError(e); this.searchStoreLoading = false; this.cdr.detectChanges(); }
    });
  }
  clearStoreSearch(): void {
    this.searchStoreId = null; this.searchStoreResults = [];
    this.searchStoreError = ''; this.storeDeleteErrors = {}; this.storeDeleteSuccess = '';
  }

  // Delete staff from the store-results panel — error inline under that row
  deleteFromStoreResults(staff: any): void {
    if (!confirm(`Delete Staff #${staff.staffId} — ${staff.firstName} ${staff.lastName}?`)) return;
    delete this.storeDeleteErrors[staff.staffId];
    this.svc.delete(staff.staffId).subscribe({
      next: () => {
        this.storeDeleteSuccess = `Staff "${staff.firstName} ${staff.lastName}" deleted.`;
        this.searchStoreResults = this.searchStoreResults.filter(s => s.staffId !== staff.staffId);
        this.items = this.items.filter(s => s.staffId !== staff.staffId);
        this.filteredItems = this.filteredItems.filter(s => s.staffId !== staff.staffId);
        this.paginate();
        setTimeout(() => this.storeDeleteSuccess = '', 3000);
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        this.storeDeleteErrors[staff.staffId] = formatBackendError(e);
        this.cdr.detectChanges();
      }
    });
  }

  // ── CRUD modal ──────────────────────────────────────────────────────
  openCreate(): void {
    this.editItem = null;
    this.formData = { firstName: '', lastName: '', email: '', username: '', password: '', addressId: null, storeId: null, active: true };
    this.modalError = ''; this.showModal = true;
  }
  openEdit(item: any): void {
    this.editItem = item;
    this.formData = { firstName: item.firstName, lastName: item.lastName, email: item.email, username: item.username, password: '', addressId: item.addressId, storeId: item.storeId, active: item.active };
    this.modalError = ''; this.showModal = true;
  }
  closeModal(): void { this.showModal = false; this.modalError = ''; }

  validate(): boolean {
    if (!this.formData.firstName?.trim())  { this.modalError = 'First name is required.'; return false; }
    if (!this.formData.lastName?.trim())   { this.modalError = 'Last name is required.'; return false; }
    if (!this.formData.email?.trim())      { this.modalError = 'Email is required.'; return false; }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.formData.email)) { this.modalError = 'Please enter a valid email address.'; return false; }
    if (!this.formData.username?.trim())   { this.modalError = 'Username is required.'; return false; }
    if (!this.editItem && !this.formData.password?.trim()) { this.modalError = 'Password is required when creating a staff member.'; return false; }
    if (!this.formData.addressId || this.formData.addressId <= 0) { this.modalError = 'A valid Address ID is required.'; return false; }
    if (!this.formData.storeId   || this.formData.storeId   <= 0) { this.modalError = 'A valid Store ID is required.'; return false; }
    return true;
  }

  save(): void {
    this.modalError = '';
    if (!this.validate()) return;
    const call = this.editItem ? this.svc.update(this.editItem.staffId, this.formData) : this.svc.create(this.formData);
    call.subscribe({
      next: () => {
        this.successMsg = `Staff ${this.editItem ? 'updated' : 'created'}!`;
        this.closeModal(); this.load(); setTimeout(() => this.successMsg = '', 3000);
      },
      error: (e: any) => { this.modalError = formatBackendError(e); }
    });
  }

  // Main table delete — error shown inline under the row
  delete(item: any): void {
    if (!confirm(`Delete Staff #${item.staffId} — ${item.firstName} ${item.lastName}?`)) return;
    delete this.tableDeleteErrors[item.staffId];
    this.svc.delete(item.staffId).subscribe({
      next: () => {
        this.successMsg = `Staff "${item.firstName} ${item.lastName}" deleted.`;
        this.load(); setTimeout(() => this.successMsg = '', 3000);
      },
      error: (e: any) => {
        this.tableDeleteErrors[item.staffId] = formatBackendError(e);
        this.cdr.detectChanges();
      }
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
