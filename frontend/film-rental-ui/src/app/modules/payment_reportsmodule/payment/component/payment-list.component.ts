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

  // All payments
  items: any[] = [];
  filteredItems: any[] = [];
  pagedItems: any[] = [];

  // Pagination
  currentPage = 1;
  pageSize = 10;
  totalPages = 1;

  // Search by Payment ID
  paymentIdInput: number | null = null;
  paymentIdResult: any = null;
  paymentIdLoading = false;
  paymentIdError = '';
  showPaymentIdResult = false;

  // Search by Customer ID
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

  // Load all payments from API
  loadAll(): void {
    this.loading = true;
    this.error = '';
    this.svc.getAll().subscribe({
      next: (data: any[]) => {
        this.items = data;
        this.filteredItems = [...data];
        this.currentPage = 1;
        this.paginate();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        this.error = e.status === 403
          ? 'Access denied.'
          : e.error?.message || 'Failed to load payments.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  // Search by Payment ID
  searchByPaymentId(): void {
    const id = this.paymentIdInput;
    if (!id || id <= 0) {
      this.paymentIdError = 'Enter a valid Payment ID.';
      return;
    }
    this.paymentIdLoading = true;
    this.paymentIdError = '';
    this.paymentIdResult = null;
    this.showPaymentIdResult = false;

    this.svc.getById(id).subscribe({
      next: (data: any) => {
        this.paymentIdResult = data;
        this.showPaymentIdResult = true;
        this.paymentIdLoading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        this.paymentIdError = e.status === 404
          ? `Payment ID ${id} not found.`
          : e.error?.message || 'Failed to fetch payment.';
        this.paymentIdLoading = false;
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

  // Search by Customer ID — filters from already loaded items
  searchByCustomerId(): void {
    const id = this.customerIdInput;
    if (!id || id <= 0) {
      this.customerIdError = 'Enter a valid Customer ID.';
      return;
    }

    this.customerIdError = '';
    this.showCustomerPayments = false;
    this.customerPayments = [];
    this.customerPaymentsFiltered = [];
    this.customerPaymentsPagedItems = [];

    const results = this.items.filter((p: any) => Number(p.customerId) === id);

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

  // Customer payments pagination
  paginateCustomer(): void {
    this.customerPaymentsTotalPages = Math.max(1, Math.ceil(this.customerPaymentsFiltered.length / this.pageSize));
    if (this.customerPaymentsPage > this.customerPaymentsTotalPages) {
      this.customerPaymentsPage = this.customerPaymentsTotalPages;
    }
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

  // Format date columns cleanly
  formatValue(key: string, val: any): string {
    if (val === null || val === undefined) return '';
    if (key.toLowerCase().includes('date') && typeof val === 'string') {
      const d = new Date(val);
      if (!isNaN(d.getTime())) {
        return d.toLocaleDateString('en-GB', {
          day: '2-digit', month: 'short', year: 'numeric',
          hour: '2-digit', minute: '2-digit', hour12: true
        });
      }
    }
    return String(val);
  }

  // Helpers
  isSimple(val: any): boolean { return val === null || val === undefined || typeof val !== 'object'; }
  keys(item: any): string[] { return Object.keys(item).slice(0, 7); }
  resultKeys(item: any): string[] { return Object.keys(item).filter(k => k !== 'lastUpdate'); }

  // Open create modal
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

  // Validate form before saving
  validate(): boolean {
    if (!this.formData.customerId) {
      this.error = 'Customer ID is required.'; return false;
    }
    if (!this.formData.staffId) {
      this.error = 'Staff ID is required.'; return false;
    }
    if (this.formData.rentalId && this.formData.rentalId <= 0) {
      this.error = 'Rental ID must be positive.'; return false;
    }
    if (!this.formData.amount || this.formData.amount <= 0) {
      this.error = 'Amount must be greater than 0.'; return false;
    }
    // DB column is DECIMAL(5,2) — max allowed value is 999.99
    if (this.formData.amount > 999.99) {
      this.error = 'Amount cannot exceed 999.99.'; return false;
    }
    return true;
  }

  // Save new payment
  save(): void {
    this.error = '';
    if (!this.validate()) return;

    const payload: any = {
      customerId:  this.formData.customerId,
      staffId:     this.formData.staffId,
      amount:      this.formData.amount,
      paymentDate: this.formData.paymentDate.length === 16
        ? this.formData.paymentDate + ':00'
        : this.formData.paymentDate
    };

    // Only include rentalId if provided
    if (this.formData.rentalId) {
      payload.rentalId = this.formData.rentalId;
    }

    this.svc.create(payload).subscribe({
      next: () => {
        this.successMsg = 'Payment created!';
        this.closeModal();
        this.loadAll();
        setTimeout(() => { this.successMsg = ''; this.cdr.detectChanges(); }, 3000);
      },
      error: (e: any) => {
        this.error = e.error?.message || e.message || 'Create failed.';
        this.cdr.detectChanges();
      }
    });
  }

  // Delete payment
  delete(item: any): void {
    if (!confirm(`Delete Payment #${item.paymentId}?`)) return;
    this.svc.delete(item.paymentId).subscribe({
      next: () => {
        this.successMsg = 'Payment deleted!';
        this.loadAll();
        setTimeout(() => { this.successMsg = ''; this.cdr.detectChanges(); }, 3000);
      },
      error: (e: any) => {
        this.error = e.error?.message || 'Delete failed.';
        this.cdr.detectChanges();
      }
    });
  }

  // All payments pagination
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