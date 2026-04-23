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
  loading = true; error = ''; showModal = false; editItem: any = null; formData: any = {}; successMsg = '';

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

  openCreate(): void { this.editItem = null; this.formData = { firstName: '', lastName: '' }; this.error = ''; this.showModal = true; }
  openEdit(item: any): void { this.editItem = item; this.formData = { firstName: item.firstName, lastName: item.lastName }; this.error = ''; this.showModal = true; }
  closeModal(): void { this.showModal = false; this.error = ''; }

  validate(): boolean {
    if (!this.formData.firstName?.trim()) { this.error = 'First name is required.'; return false; }
    if (!this.formData.lastName?.trim()) { this.error = 'Last name is required.'; return false; }
    if (/\d/.test(this.formData.firstName) || /\d/.test(this.formData.lastName)) { this.error = 'Names must not contain numbers.'; return false; }
    return true;
  }

  save(): void {
    this.error = '';
    if (!this.validate()) return;
    const payload = { firstName: this.formData.firstName.trim(), lastName: this.formData.lastName.trim() };
    const call = this.editItem ? this.svc.update(this.editItem.actorId, payload) : this.svc.create(payload);
    call.subscribe({
      next: () => { this.successMsg = `Actor ${this.editItem ? 'updated' : 'created'}!`; this.closeModal(); this.load(); setTimeout(() => this.successMsg = '', 3000); },
      error: (e: any) => { this.error = e.error?.message || e.error?.error || e.message || 'Operation failed'; }
    });
  }

  delete(item: any): void {
    if (!confirm('Delete this Actor?')) return;
    this.error = '';
    this.svc.delete(item.actorId).subscribe({
      next: () => { this.successMsg = 'Actor deleted!'; this.load(); setTimeout(() => this.successMsg = '', 3000); },
      error: (e: any) => { this.error = e.error?.message || e.error?.error || 'Delete failed'; }
    });
  }

  search(term: string): void {
    this.searchTerm = term;
    const trimmed = term.trim();
    const numId = Number(trimmed);

    if (trimmed !== '' && !isNaN(numId) && Number.isInteger(numId) && numId > 0) {
      // Numeric input — fetch by ID from API, then also filter by name locally
      this.loading = true;
      this.svc.getById(numId).subscribe({
        next: (item: any) => {
          this.filteredItems = [item];
          this.currentPage = 1; this.paginate(); this.loading = false; this.cdr.detectChanges();
        },
        error: () => {
          this.filteredItems = this.items.filter(i =>
            JSON.stringify(i).toLowerCase().includes(trimmed.toLowerCase())
          );
          this.currentPage = 1; this.paginate(); this.loading = false; this.cdr.detectChanges();
        }
      });
    } else {
      // Text input — filter locally by name
      this.filteredItems = this.items.filter(item =>
        JSON.stringify(item).toLowerCase().includes(trimmed.toLowerCase())
      );
      this.currentPage = 1; this.paginate();
    }
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
