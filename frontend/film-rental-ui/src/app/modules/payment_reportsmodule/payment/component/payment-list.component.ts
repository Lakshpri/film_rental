import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PaymentService } from '../service/payment.service';
 
@Component({
  standalone: true,
  selector: 'app-payment-list',
  templateUrl: './payment-list.component.html',
  imports: [CommonModule, FormsModule]
})
export class PaymentListComponent implements OnInit {
  // All payments (default view)
  items: any[] = [];
  filteredItems: any[] = [];
  pagedItems: any[] = [];
 
  // Pagination
  currentPage = 1;
  pageSize = 10;
  totalPages = 1;
 
  // Search bar 1 — GET /api/payments/{paymentId}
  paymentIdInput: number | null = null;
  paymentIdResult: any = null;
  paymentIdLoading = false;
  paymentIdError = '';
  showPaymentIdResult = false;
 
  // Search bar 2 — filter from loaded items by customerId
  customerIdInput: number | null = null;
  customerPayments: any[] = [];
  customerPaymentsFiltered: any[] = [];
  customerPaymentsPagedItems: any[] = [];
  customerPaymentsPage = 1;
  customerPaymentsTotalPages = 1;
  customerIdLoading = false;
  customerIdError = '';
  showCustomerPayments = false;
 
  // UI state
  loading = true;
  error = '';
  showModal = false;
  formData: any = {};
  successMsg = '';
 
  constructor(private svc: PaymentService, private cdr: ChangeDetectorRef) {}
 
  ngOnInit(): void { this.loadAll(); }
 
  // ── Load all payments ─────────────────────────────────────────────
  loadAll(): void {
    this.loading = true;
    this.error = '';
    this.svc.getAll().subscribe({
      next: (d: any[]) => {
        this.items = d;
        this.filteredItems = [...d];
        this.currentPage = 1;
        this.paginate();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        this.error = e.status === 403
          ? 'Access denied. Only the Payment & Reports user can access payments.'
          : e.error?.message || e.message || 'Failed to load payments.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }
 
  // ── Search Bar 1: GET /api/payments/{paymentId} ───────────────────
  searchByPaymentId(): void {
    const id = Number(this.paymentIdInput);
    if (!id || id <= 0) {
      this.paymentIdError = 'Enter a valid Payment ID.';
      return;
    }
    this.paymentIdLoading = true;
    this.paymentIdError = '';
    this.paymentIdResult = null;
    this.showPaymentIdResult = false;
 
    this.svc.getById(id).subscribe({
      next: (d: any) => {
        this.paymentIdResult = d;
        this.showPaymentIdResult = true;
        this.paymentIdLoading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        this.paymentIdError = e.status === 404
          ? `Payment ID ${id} not found.`
          : e.error?.message || e.message || 'Failed to fetch payment.';
        this.paymentIdLoading = false;
        this.showPaymentIdResult = false;
        this.cdr.detectChanges();
      }
    });
  }
 
  clearPaymentIdSearch(): void {
    this.paymentIdInput = null;
    this.paymentIdResult = null;
    this.paymentIdError = '';
    this.showPaymentIdResult = false;
  }
 
  // ── Search Bar 2: Filter from already-loaded items by customerId ──
  searchByCustomerId(): void {
    const id = Number(this.customerIdInput);
    if (!id || id <= 0) {
      this.customerIdError = 'Enter a valid Customer ID.';
      return;
    }
 
    this.customerIdError = '';
    this.showCustomerPayments = false;
    this.customerPayments = [];
    this.customerPaymentsFiltered = [];
    this.customerPaymentsPagedItems = [];
 
    const results = this.items.filter(
      (p: any) => Number(p.customerId) === id
    );
 
    if (results.length === 0) {
      this.customerIdError = `No payments found for Customer ID ${id}.`;
      this.cdr.detectChanges();
      return;
    }
 
    this.customerPayments = results;
    this.customerPaymentsFiltered = [...results];
    this.customerPaymentsPage = 1;
    this.paginateCustomer();
    this.showCustomerPayments = true;
    this.cdr.detectChanges();
  }
 
  clearCustomerIdSearch(): void {
    this.customerIdInput = null;
    this.customerPayments = [];
    this.customerPaymentsFiltered = [];
    this.customerPaymentsPagedItems = [];
    this.customerIdError = '';
    this.showCustomerPayments = false;
  }
 
  paginateCustomer(): void {
    this.customerPaymentsTotalPages = Math.max(1, Math.ceil(this.customerPaymentsFiltered.length / this.pageSize));
    if (this.customerPaymentsPage > this.customerPaymentsTotalPages) this.customerPaymentsPage = this.customerPaymentsTotalPages;
    const start = (this.customerPaymentsPage - 1) * this.pageSize;
    this.customerPaymentsPagedItems = this.customerPaymentsFiltered.slice(start, start + this.pageSize);
  }
 
  goToCustomerPage(page: number): void {
    if (page < 1 || page > this.customerPaymentsTotalPages) return;
    this.customerPaymentsPage = page;
    this.paginateCustomer();
  }
 
  get customerPageNumbers(): number[] {
    const pages: number[] = [];
    const start = Math.max(1, this.customerPaymentsPage - 2);
    const end = Math.min(this.customerPaymentsTotalPages, start + 4);
    for (let i = start; i <= end; i++) pages.push(i);
    return pages;
  }
 
  // ── Format cell values — cleans up dates ──────────────────────────
  formatValue(key: string, val: any): string {
    if (val === null || val === undefined) return '';
    if (key.toLowerCase().includes('date') && typeof val === 'string') {
      const d = new Date(val);
      if (!isNaN(d.getTime())) {
        return d.toLocaleDateString('en-GB', {
          day: '2-digit',
          month: 'short',
          year: 'numeric',
          hour: '2-digit',
          minute: '2-digit',
          hour12: true
        });
      }
    }
    return String(val);
  }
 
  // ── Helpers ────────────────────────────────────────────────────────
  isSimple(val: any): boolean { return val === null || val === undefined || typeof val !== 'object'; }
  keys(item: any): string[] { return Object.keys(item).slice(0, 7); }
  resultKeys(item: any): string[] { return Object.keys(item).filter(k => k !== 'lastUpdate'); }
 
  // ── Create ────────────────────────────────────────────────────────
  openCreate(): void {
    this.formData = {
      customerId:  null,
      staffId:     null,
      rentalId:    null,
      amount:      null,
      paymentDate: new Date().toISOString().slice(0, 16)
    };
    this.error = '';
    this.showModal = true;
  }
 
  closeModal(): void { this.showModal = false; this.error = ''; }
 
  validate(): boolean {
    if (!this.formData.customerId || Number(this.formData.customerId) <= 0) {
      this.error = 'Customer ID is required.'; return false;
    }
    if (!this.formData.staffId || Number(this.formData.staffId) <= 0) {
      this.error = 'Staff ID is required.'; return false;
    }
    if (this.formData.rentalId !== null && this.formData.rentalId !== '' &&
        Number(this.formData.rentalId) <= 0) {
      this.error = 'Rental ID must be positive if provided.'; return false;
    }
    if (!this.formData.amount || Number(this.formData.amount) <= 0) {
      this.error = 'Amount must be greater than 0.'; return false;
    }
    return true;
  }
 
  save(): void {
    this.error = '';
    if (!this.validate()) return;
    const payload: any = {
      customerId:  Number(this.formData.customerId),
      staffId:     Number(this.formData.staffId),
      amount:      Number(this.formData.amount),
      paymentDate: this.formData.paymentDate.length === 16
        ? this.formData.paymentDate + ':00'
        : this.formData.paymentDate
    };
    if (this.formData.rentalId !== null && this.formData.rentalId !== '') {
      payload.rentalId = Number(this.formData.rentalId);
    }
    this.svc.create(payload).subscribe({
      next: () => {
        this.successMsg = 'Payment created!';
        this.closeModal();
        this.loadAll();
        setTimeout(() => { this.successMsg = ''; this.cdr.detectChanges(); }, 3000);
      },
      error: (e: any) => {
        this.error = e.error?.message || e.error?.error || e.message || 'Create failed';
        this.cdr.detectChanges();
      }
    });
  }
 
  // ── Delete ────────────────────────────────────────────────────────
  delete(item: any): void {
    if (!confirm(`Delete Payment #${item.paymentId}?`)) return;
    this.svc.delete(item.paymentId).subscribe({
      next: () => {
        this.successMsg = 'Payment deleted!';
        this.loadAll();
        setTimeout(() => { this.successMsg = ''; this.cdr.detectChanges(); }, 3000);
      },
      error: (e: any) => {
        this.error = e.error?.message || 'Delete failed';
        this.cdr.detectChanges();
      }
    });
  }
 
  // ── Pagination (all payments table) ───────────────────────────────
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