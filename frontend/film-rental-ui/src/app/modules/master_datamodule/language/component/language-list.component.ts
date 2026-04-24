import { formatBackendError } from '../../../../shared/error-utils';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LanguageService } from '../service/language.service';

@Component({
  standalone: true,
  selector: 'app-language-list',
  templateUrl: './language-list.component.html',
  imports: [CommonModule, FormsModule]
})
export class LanguageListComponent implements OnInit {
  items: any[] = []; filteredItems: any[] = []; pagedItems: any[] = [];
  currentPage = 1; pageSize = 10; totalPages = 1; searchTerm = '';
  loading = true; error = ''; showModal = false; editItem: any = null; formData: any = {}; successMsg = '';

  constructor(private svc: LanguageService, private cdr: ChangeDetectorRef) {}
  ngOnInit(): void { setTimeout(() => this.load()); }

  load(): void {
    this.loading = true; this.error = '';
    this.svc.getAll().subscribe({
      next: (d: any[]) => { this.items = d; this.filteredItems = d; this.paginate(); this.loading = false; this.cdr.detectChanges(); },
      error: (e: any) => { this.error = formatBackendError(e); this.loading = false; this.cdr.detectChanges(); }
    });
  }

  openCreate(): void { this.editItem = null; this.formData = { name: '' }; this.error = ''; this.showModal = true; }
  openEdit(item: any): void { this.editItem = item; this.formData = { name: item.name }; this.error = ''; this.showModal = true; }
  closeModal(): void { this.showModal = false; this.error = ''; }

  validate(): boolean {
    if (!this.formData.name?.trim()) { this.error = 'Language name is required.'; this.cdr.detectChanges(); return false; }
    return true;
  }

  save(): void {
    this.error = '';
    if (!this.validate()) return;
    const call = this.editItem
      ? this.svc.update(this.editItem.languageId, { name: this.formData.name.trim() })
      : this.svc.create({ name: this.formData.name.trim() });
    call.subscribe({
      next: () => {
        this.successMsg = `Language ${this.editItem ? 'updated' : 'created'}!`;
        this.closeModal(); this.load();
        setTimeout(() => { this.successMsg = ''; this.cdr.detectChanges(); }, 3000);
      },
      error: (e: any) => { this.error = formatBackendError(e); this.cdr.detectChanges(); }
    });
  }

  delete(item: any): void {
    if (!confirm('Delete this Language?')) return;
    this.error = '';
    this.svc.delete(item.languageId).subscribe({
      next: () => { this.successMsg = 'Language deleted!'; this.load(); setTimeout(() => { this.successMsg = ''; this.cdr.detectChanges(); }, 3000); },
      error: (e: any) => { this.error = formatBackendError(e); this.cdr.detectChanges(); }
    });
  }

  search(term: string): void {
    this.searchTerm = term.trim();
    if (!this.searchTerm) { this.filteredItems = [...this.items]; this.currentPage = 1; this.paginate(); return; }
    if (!isNaN(Number(this.searchTerm))) {
      const id = Number(this.searchTerm);
      this.loading = true;
      this.svc.getById(id).subscribe({
        next: (res: any) => { this.filteredItems = res ? [res] : []; this.currentPage = 1; this.paginate(); this.loading = false; this.cdr.detectChanges(); },
        error: () => { this.filteredItems = []; this.currentPage = 1; this.paginate(); this.loading = false; this.cdr.detectChanges(); }
      });
    } else {
      const lower = this.searchTerm.toLowerCase();
      this.filteredItems = this.items.filter(item => item.name?.toLowerCase().includes(lower));
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