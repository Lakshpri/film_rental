import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FilmTextService } from '../service/film-text.service';

@Component({
  standalone: true,
  selector: 'app-film-text-list',
  templateUrl: './film-text-list.component.html',
  imports: [CommonModule, FormsModule]
})
export class FilmTextListComponent implements OnInit {
  items: any[] = []; filteredItems: any[] = []; pagedItems: any[] = [];
  currentPage = 1; pageSize = 10; totalPages = 1; searchTerm = '';
  loading = true; error = ''; showModal = false; editItem: any = null; formData: any = {}; successMsg = '';

  constructor(private svc: FilmTextService, private cdr: ChangeDetectorRef) {}
  ngOnInit(): void { setTimeout(() => this.load()); }

  load(): void {
    this.loading = true;
    this.error = '';
    this.svc.getAll().subscribe({
      next: (data: any[]) => { this.items = data; this.filteredItems = data; this.paginate(); this.loading = false; this.cdr.detectChanges(); },
      error: (e: any) => { this.error = e.error?.message || e.error?.error || e.message || 'Failed to load film texts.'; this.loading = false; this.cdr.detectChanges(); }
    });
  }

  openCreate(): void { this.editItem = null; this.formData = { filmId: null, title: '', description: '' }; this.error = ''; this.showModal = true; }
  openEdit(item: any): void { this.editItem = item; this.formData = { filmId: item.filmId, title: item.title, description: item.description }; this.error = ''; this.showModal = true; }
  closeModal(): void { this.showModal = false; this.error = ''; }

  validate(): boolean {
    if (!this.formData.filmId || this.formData.filmId <= 0) { this.error = 'A valid Film ID is required.'; return false; }
    if (!this.formData.title?.trim()) { this.error = 'Title is required.'; return false; }
    return true;
  }

  save(): void {
    this.error = '';
    if (!this.validate()) return;
    const payload = { filmId: this.formData.filmId, title: this.formData.title.trim(), description: this.formData.description };
    const call = this.editItem ? this.svc.update(this.editItem.filmId, payload) : this.svc.create(payload);
    call.subscribe({
      next: () => { this.successMsg = `Film Text ${this.editItem ? 'updated' : 'created'}!`; this.closeModal(); this.load(); setTimeout(() => this.successMsg = '', 3000); },
      error: (e: any) => { this.error = e.error?.message || e.error?.error || e.message || 'Operation failed'; }
    });
  }

  delete(item: any): void {
    if (!confirm('Delete this Film Text?')) return;
    this.error = '';
    this.svc.delete(item.filmId).subscribe({
      next: () => { this.successMsg = 'Film Text deleted!'; this.load(); setTimeout(() => this.successMsg = '', 3000); },
      error: (e: any) => { this.error = e.error?.message || e.error?.error || 'Delete failed'; }
    });
  }

  search(term: string): void {
    this.searchTerm = term;
    const trimmed = term.trim();
    const numId = Number(trimmed);

    if (trimmed !== '' && !isNaN(numId) && Number.isInteger(numId) && numId > 0) {
      // Numeric input — fetch by film ID from API, then also filter by title locally
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
      // Text input — filter locally by title
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
