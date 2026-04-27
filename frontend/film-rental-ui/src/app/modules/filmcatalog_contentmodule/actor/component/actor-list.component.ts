import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActorService } from '../service/actor.service';

@Component({
  standalone: true,
  selector: 'app-actor-list',
  templateUrl: './actor-list.component.html',
  imports: [CommonModule, FormsModule]
})
export class ActorListComponent implements OnInit {
  items: any[] = []; filteredItems: any[] = []; pagedItems: any[] = [];
  currentPage = 1; pageSize = 10; totalPages = 1; searchTerm = '';
  idSearchTerm = '';
  loading = true; error = ''; showModal = false; editItem: any = null; formData: any = {}; successMsg = '';
  modalError = ''; formErrors: { [key: string]: string } = {};

  constructor(private svc: ActorService, private cdr: ChangeDetectorRef) {}
  ngOnInit(): void { setTimeout(() => this.load()); }

  load(): void {
    this.loading = true;
    this.error = '';
    this.svc.getAll().subscribe({
      next: (data: any[]) => { this.items = data; this.filteredItems = data; this.paginate(); this.loading = false; this.cdr.detectChanges(); },
      error: (e: any) => { this.error = e.error?.message || e.error?.error || e.message || 'Failed to load actors.'; this.loading = false; this.cdr.detectChanges(); }
    });
  }

  openCreate(): void { this.editItem = null; this.formData = { firstName: '', lastName: '' }; this.modalError = ''; this.formErrors = {}; this.showModal = true; }
  openEdit(item: any): void { this.editItem = item; this.formData = { firstName: item.firstName, lastName: item.lastName }; this.modalError = ''; this.formErrors = {}; this.showModal = true; }
  closeModal(): void { this.showModal = false; this.modalError = ''; this.formErrors = {}; }

  validate(): boolean {
    this.formErrors = {};
    this.modalError = '';
    if (!this.formData.firstName?.trim()) { this.formErrors['firstName'] = 'First name is required.'; }
    else if (/\d/.test(this.formData.firstName)) { this.formErrors['firstName'] = 'First name must not contain numbers.'; }
    if (!this.formData.lastName?.trim()) { this.formErrors['lastName'] = 'Last name is required.'; }
    else if (/\d/.test(this.formData.lastName)) { this.formErrors['lastName'] = 'Last name must not contain numbers.'; }
    if (Object.keys(this.formErrors).length > 0) {
      this.modalError = 'Please fix the highlighted fields and try again.';
      return false;
    }
    return true;
  }

  parseBackendError(e: any): void {
    const err = e.error;
    if (err?.fields && typeof err.fields === 'object') {
      this.formErrors = { ...err.fields };
      this.modalError = err.error || err.message || 'Some fields have invalid values.';
    } else {
      this.modalError = err?.message || err?.error || err?.reason || e.message || 'Operation failed.';
    }
  }

  save(): void {
    this.modalError = '';
    this.formErrors = {};
    if (!this.validate()) return;
    const payload = { firstName: this.formData.firstName.trim(), lastName: this.formData.lastName.trim() };
    const call = this.editItem ? this.svc.update(this.editItem.actorId, payload) : this.svc.create(payload);
    call.subscribe({
      next: () => { this.successMsg = `Actor ${this.editItem ? 'updated' : 'created'}!`; this.closeModal(); this.load(); setTimeout(() => this.successMsg = '', 3000); },
      error: (e: any) => { this.parseBackendError(e); this.cdr.detectChanges(); }
    });
  }

  delete(item: any): void {
    if (!confirm('Delete this Actor?')) return;
    this.error = '';
    this.svc.delete(item.actorId).subscribe({
      next: () => { this.successMsg = 'Actor deleted!'; this.load(); setTimeout(() => this.successMsg = '', 3000); },
      error: (e: any) => { this.error = e.error?.reason || e.error?.message || e.error?.error || 'Delete failed'; }
    });
  }

  searchById(term: string): void {
    const raw = term.trim();
    this.idSearchTerm = raw;
    this.error = '';

    if (!raw) {
      this.filteredItems = [...this.items];
      if (this.searchTerm) {
        const lower = this.searchTerm.toLowerCase();
        this.filteredItems = this.filteredItems.filter(item =>
          item.firstName?.toLowerCase().includes(lower) ||
          item.lastName?.toLowerCase().includes(lower)
        );
      }
      this.currentPage = 1;
      this.paginate();
      return;
    }

    if (isNaN(Number(raw)) || raw === '') return;

    const id = Number(raw);

    // Added only this validation
    if (id <= 0) {
      this.error = 'Actor ID must be a positive number';
      this.filteredItems = [];
      this.currentPage = 1;
      this.paginate();
      return;
    }

    this.loading = true;

    this.svc.getById(id).subscribe({
      next: (res: any) => {
        this.filteredItems = res ? [res] : [];
        this.currentPage = 1;
        this.paginate();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        this.error = e.error?.message || e.error?.error || e.message;
        this.filteredItems = [];
        this.currentPage = 1;
        this.paginate();
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  search(term: string): void {
    this.searchTerm = term.trim();
    this.error = '';

    if (this.idSearchTerm) return;

    if (!this.searchTerm) {
      this.filteredItems = [...this.items];
      this.currentPage = 1;
      this.paginate();
      return;
    }

    const lower = this.searchTerm.toLowerCase();
    this.filteredItems = this.items.filter(item =>
      item.firstName?.toLowerCase().includes(lower) ||
      item.lastName?.toLowerCase().includes(lower)
    );

    this.currentPage = 1;
    this.paginate();
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

export default ActorListComponent;