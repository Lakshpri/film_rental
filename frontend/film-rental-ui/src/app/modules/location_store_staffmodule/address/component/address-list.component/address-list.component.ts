import { formatBackendError } from '../../../../shared/error-utils';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AddressService } from '../../service/address.service';

@Component({
  standalone: true,
  selector: 'app-address-list.component',
  imports: [CommonModule,FormsModule],
  templateUrl: './address-list.component.html',
  styleUrl: './address-list.component.css',
})
export class AddressListComponent {
  items: any[] = []; filteredItems: any[] = []; pagedItems: any[] = [];
  currentPage = 1; pageSize = 10; totalPages = 1; searchTerm = '';
  loading = true; error = ''; showModal = false; editItem: any = null; formData: any = {}; successMsg = '';
  modalError = '';

  // Search by Address ID
  searchAddressId: number | null = null;
  searchAddressIdResult: any = null;
  searchAddressIdError = '';
  searchAddressIdLoading = false;

  // Search by City ID — filtered client-side from loaded data (avoids role permission issue)
  searchCityId: number | null = null;
  searchCityIdResults: any[] = [];
  searchCityIdError = '';
  cityIdSearched: number | null = null;

  // Per-row delete errors for city search results
  cityDeleteErrors: { [addressId: number]: string } = {};
  cityDeleteSuccess = '';

  constructor(private svc: AddressService, private cdr: ChangeDetectorRef) {}
  ngOnInit(): void { setTimeout(() => this.load()); }

  load(): void {
    this.loading = true; this.error = '';
    this.svc.getAll().subscribe({
      next: (d: any[]) => {
        this.items = d; this.filteredItems = d; this.paginate();
        this.loading = false;
        // Re-apply city filter if active
        if (this.cityIdSearched !== null) { this.applyCity(); }
        this.cdr.detectChanges();
      },
      error: (e: any) => { this.error = formatBackendError(e); this.loading = false; this.cdr.detectChanges(); }
    });
  }

  // ── Search by Address ID ────────────────────────────────────────────
  fetchByAddressId(): void {
    if (!this.searchAddressId || this.searchAddressId <= 0) {
      this.searchAddressIdError = 'Please enter a valid Address ID (positive number).';
      this.searchAddressIdResult = null; return;
    }
    this.searchAddressIdLoading = true; this.searchAddressIdError = ''; this.searchAddressIdResult = null;
    this.svc.getById(this.searchAddressId).subscribe({
      next: (d: any) => { this.searchAddressIdResult = d; this.searchAddressIdLoading = false; this.cdr.detectChanges(); },
      error: (e: any) => { this.searchAddressIdError = formatBackendError(e); this.searchAddressIdLoading = false; this.cdr.detectChanges(); }
    });
  }
  clearAddressIdSearch(): void { this.searchAddressId = null; this.searchAddressIdResult = null; this.searchAddressIdError = ''; }

  // ── Search by City ID — client-side filter (no extra API call, no role issue) ──
  fetchByCityId(): void {
    if (!this.searchCityId || this.searchCityId <= 0) {
      this.searchCityIdError = 'Please enter a valid City ID (positive number).';
      this.searchCityIdResults = []; this.cityIdSearched = null; return;
    }
    this.searchCityIdError = '';
    this.cityDeleteErrors = {};
    this.cityDeleteSuccess = '';
    this.applyCity();
  }

  private applyCity(): void {
    this.cityIdSearched = this.searchCityId;
    this.searchCityIdResults = this.items.filter(a => a.cityId === this.searchCityId);
    if (this.searchCityIdResults.length === 0) {
      this.searchCityIdError = `No addresses found for City ID ${this.searchCityId}.`;
    }
    this.cdr.detectChanges();
  }

  clearCityIdSearch(): void {
    this.searchCityId = null; this.cityIdSearched = null;
    this.searchCityIdResults = []; this.searchCityIdError = '';
    this.cityDeleteErrors = {}; this.cityDeleteSuccess = '';
  }

  // ── Delete from city search results (inline per-row error) ──────────
  deleteFromCityResults(item: any): void {
    if (!confirm(`Delete Address #${item.addressId}?`)) return;
    delete this.cityDeleteErrors[item.addressId];
    this.svc.delete(item.addressId).subscribe({
      next: () => {
        this.cityDeleteSuccess = `Address #${item.addressId} deleted successfully.`;
        this.searchCityIdResults = this.searchCityIdResults.filter(a => a.addressId !== item.addressId);
        this.items = this.items.filter(a => a.addressId !== item.addressId);
        this.filteredItems = this.filteredItems.filter(a => a.addressId !== item.addressId);
        this.paginate();
        setTimeout(() => this.cityDeleteSuccess = '', 3000);
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        this.cityDeleteErrors[item.addressId] = formatBackendError(e);
        this.cdr.detectChanges();
      }
    });
  }

  // ── CRUD ─────────────────────────────────────────────────────────────
  openCreate(): void { this.editItem = null; this.formData = { address: '', address2: '', district: '', postalCode: '', phone: '', cityId: null }; this.modalError = ''; this.showModal = true; }
  openEdit(item: any): void { this.editItem = item; this.formData = { address: item.address, address2: item.address2, district: item.district, postalCode: item.postalCode, phone: item.phone, cityId: item.cityId }; this.modalError = ''; this.showModal = true; }
  closeModal(): void { this.showModal = false; this.modalError = ''; }

  validate(): boolean {
    if (!this.formData.address?.trim()) { this.modalError = 'Address line 1 is required.'; return false; }
    if (!this.formData.cityId || this.formData.cityId <= 0) { this.modalError = 'A valid City ID is required.'; return false; }
    if (this.formData.phone && !/^[0-9\-\+\s\(\)]{6,20}$/.test(this.formData.phone)) { this.modalError = 'Phone number format is invalid.'; return false; }
    return true;
  }

  save(): void {
    this.modalError = '';
    if (!this.validate()) return;
    const call = this.editItem ? this.svc.update(this.editItem.addressId, this.formData) : this.svc.create(this.formData);
    call.subscribe({
      next: () => {
        this.successMsg = `Address ${this.editItem ? 'updated' : 'created'}!`;
        this.closeModal(); this.load(); setTimeout(() => this.successMsg = '', 3000);
      },
      error: (e: any) => { this.modalError = formatBackendError(e); }
    });
  }

  delete(item: any): void {
    if (!confirm('Delete this Address?')) return;
    this.error = '';
    this.svc.delete(item.addressId).subscribe({
      next: () => { this.successMsg = 'Address deleted!'; this.load(); setTimeout(() => this.successMsg = '', 3000); },
      error: (e: any) => { this.error = formatBackendError(e); }
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
