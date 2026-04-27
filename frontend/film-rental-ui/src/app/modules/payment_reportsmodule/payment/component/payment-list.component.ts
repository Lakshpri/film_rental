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

  // Search bar 2 — GET /api/payments/customer/{customerId}
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
  fieldErrors: { [key: string]: string } = {};
  showModal = false;
  formData: any = {};
  successMsg = '';

  constructor(private svc: PaymentService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void { this.loadAll(); }

  /**
   * Parses the backend error response into a displayable string.
   *
   * When a "fields" map is present (from @Valid or @Positive),
   * the first field message is returned directly so the user sees
   * the specific constraint message instead of the generic error label.
   * fieldErrors is also populated so the form template can show
   * per-field messages next to each input.
   */
  private extractError(e: any): string {
    if (e?.error?.fields && typeof e.error.fields === 'object') {
      this.fieldErrors = e.error.fields;
      const firstMessage = Object.values(e.error.fields)[0] as string;
      return firstMessage || e.error.error || 'Some fields have invalid values.';
    }
    this.fieldErrors = {};
    return e?.error?.message
      || e?.error?.reason
      || e?.error?.error
      || e?.message
      || 'An unexpected error occurred.';
  }

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
          : this.extractError(e);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  /**
   * Calls GET /api/payments/{paymentId}.
   * Only blocks null/undefined — 0 and negative values reach the backend
   * so the @Positive constraint message is shown from the backend response.
   * Note: !id check cannot be used here because 0 is falsy in JavaScript.
   */
  searchByPaymentId(): void {
    if (this.paymentIdInput === null || this.paymentIdInput === undefined) return;
    const id = Number(this.paymentIdInput);

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
        // covers: @Positive violation, PaymentNotFoundException
        this.paymentIdError = this.extractError(e);
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

  /**
   * Calls GET /api/payments/customer/{customerId}.
   * Only blocks null/undefined — 0 and negative values reach the backend
   * so the @Positive constraint message is shown from the backend response.
   * Note: !id check cannot be used here because 0 is falsy in JavaScript.
   */
  searchByCustomerId(): void {
    if (this.customerIdInput === null || this.customerIdInput === undefined) return;
    const id = Number(this.customerIdInput);

    this.customerIdLoading = true;
    this.customerIdError = '';
    this.showCustomerPayments = false;
    this.customerPayments = [];
    this.customerPaymentsFiltered = [];
    this.customerPaymentsPagedItems = [];

    this.svc.getByCustomer(id).subscribe({
      next: (results: any[]) => {
        this.customerPayments = results;
        this.customerPaymentsFiltered = [...results];
        this.customerPaymentsPage = 1;
        this.paginateCustomer();
        this.showCustomerPayments = results.length > 0;
        this.customerIdLoading = false;
        if (results.length === 0) {
          this.customerIdError = `No payments found for Customer ID ${id}.`;
        }
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        // covers: @Positive violation, CustomerNotFoundException
        this.customerIdError = this.extractError(e);
        this.customerIdLoading = false;
        this.cdr.detectChanges();
      }
    });
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

  // ── Helpers ────────────────────────────────────────────────────────
  isSimple(val: any): boolean { return val === null || val === undefined || typeof val !== 'object'; }
  keys(item: any): string[] { return Object.keys(item).filter(k => k !== 'message').slice(0, 7); }
  resultKeys(item: any): string[] { return Object.keys(item).filter(k => k !== 'lastUpdate'); }

  formatValue(key: string, value: any): string {
    if (value === null || value === undefined) return '—';
    if (key === 'paymentDate' && typeof value === 'string') {
      const d = new Date(value);
      const day = String(d.getDate()).padStart(2, '0');
      const month = d.toLocaleString('en-US', { month: 'short' });
      const year = d.getFullYear();
      const time = d.toLocaleString('en-US', { hour: '2-digit', minute: '2-digit', hour12: true });
      return `${day}-${month}-${year} ${time}`;
    }
    if (typeof value === 'object') return JSON.stringify(value);
    return String(value);
  }

  fieldError(field: string): string {
    return this.fieldErrors[field] || '';
  }

  hasFieldError(field: string): boolean {
    return !!this.fieldErrors[field];
  }

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
    this.fieldErrors = {};
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.error = '';
    this.fieldErrors = {};
  }

  /**
   * Submits POST /api/payments.
   *
   * Null is sent for empty fields so @NotNull fires "field is required".
   * Explicit null/empty check is used instead of truthy check because
   * 0 is falsy in JavaScript — without this, typing 0 would send null
   * and trigger @NotNull instead of the correct @Positive message.
   */
  save(): void {
    this.error = '';
    this.fieldErrors = {};

    const payload: any = {
      customerId:  this.formData.customerId !== null && this.formData.customerId !== '' ? Number(this.formData.customerId) : null,
      staffId:     this.formData.staffId !== null && this.formData.staffId !== '' ? Number(this.formData.staffId) : null,
      amount:      this.formData.amount !== null && this.formData.amount !== '' ? Number(this.formData.amount) : null,
      // datetime-local gives "YYYY-MM-DDTHH:mm" — backend expects seconds
      paymentDate: this.formData.paymentDate.length === 16
        ? this.formData.paymentDate + ':00'
        : this.formData.paymentDate
    };

    // rentalId is optional — only include if the user entered a value
    if (this.formData.rentalId !== null && this.formData.rentalId !== '') {
      payload.rentalId = Number(this.formData.rentalId);
    }

    this.svc.create(payload).subscribe({
      next: (res: any) => {
        this.successMsg = res.message; // "Payment created!" from backend
        this.closeModal();
        this.loadAll();
        setTimeout(() => { this.successMsg = ''; this.cdr.detectChanges(); }, 3000);
      },
      error: (e: any) => {
        // extractError also populates fieldErrors for @Valid field-level messages
        this.error = this.extractError(e);
        this.cdr.detectChanges();
      }
    });
  }

  // ── Delete ────────────────────────────────────────────────────────
  delete(item: any): void {
    if (!confirm(`Delete Payment #${item.paymentId}?`)) return;
    this.svc.delete(item.paymentId).subscribe({
      next: (res: any) => {
        this.successMsg = res.message; // "Payment deleted Successfully!" from backend
        this.loadAll();
        setTimeout(() => { this.successMsg = ''; this.cdr.detectChanges(); }, 3000);
      },
      error: (e: any) => {
        this.error = this.extractError(e);
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