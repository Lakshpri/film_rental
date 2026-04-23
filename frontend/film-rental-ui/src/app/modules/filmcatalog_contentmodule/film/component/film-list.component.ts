import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FilmService } from '../service/film.service';

@Component({
  standalone: true,
  selector: 'app-film-list',
  templateUrl: './film-list.component.html',
  imports: [CommonModule, FormsModule]
})
export class FilmListComponent implements OnInit {
  items: any[] = []; filteredItems: any[] = []; pagedItems: any[] = [];
  currentPage = 1; pageSize = 10; totalPages = 1; searchTerm = '';
  loading = true; error = ''; showModal = false; editItem: any = null; formData: any = {}; successMsg = '';

  readonly ratingOptions = ['G', 'PG', 'PG-13', 'R', 'NC-17'];

  constructor(private svc: FilmService, private cdr: ChangeDetectorRef) {}
  ngOnInit(): void { setTimeout(() => this.load()); }

  load(): void {
    this.loading = true;
    this.error = '';
    this.svc.getAll().subscribe({
      next: (d: any[]) => { this.items = d; this.filteredItems = d; this.paginate(); this.loading = false; this.cdr.detectChanges(); },
      error: (e: any) => { this.error = e.error?.message || e.error?.error || e.message || 'Failed to load films.'; this.loading = false; this.cdr.detectChanges(); }
    });
  }

  openCreate(): void {
    this.editItem = null;
    this.formData = { title: '', description: '', releaseYear: null, languageId: null, rentalDuration: 3, rentalRate: 4.99, length: null, replacementCost: 19.99, rating: 'G', specialFeatures: '' };
    this.error = ''; this.showModal = true;
  }

  openEdit(item: any): void {
    this.editItem = item;
    this.formData = { title: item.title, description: item.description, releaseYear: item.releaseYear, languageId: item.languageId, rentalDuration: item.rentalDuration, rentalRate: item.rentalRate, length: item.length, replacementCost: item.replacementCost, rating: item.rating, specialFeatures: item.specialFeatures };
    this.error = ''; this.showModal = true;
  }

  closeModal(): void { this.showModal = false; this.error = ''; }

  validate(): boolean {
    if (!this.formData.title?.trim()) { this.error = 'Title is required.'; return false; }
    if (this.formData.languageId === null || this.formData.languageId === '') { this.error = 'Language ID is required.'; return false; }
    if (this.formData.languageId <= 0) { this.error = 'Language ID must be a positive number.'; return false; }
    if (this.formData.releaseYear && (this.formData.releaseYear < 1888 || this.formData.releaseYear > new Date().getFullYear() + 1)) {
      this.error = 'Release year is not valid.'; return false;
    }
    if (this.formData.rentalDuration < 1) { this.error = 'Rental duration must be at least 1 day.'; return false; }
    if (this.formData.rentalRate < 0) { this.error = 'Rental rate cannot be negative.'; return false; }
    if (this.formData.length !== null && this.formData.length <= 0) { this.error = 'Film length must be positive.'; return false; }
    if (this.formData.replacementCost < 0) { this.error = 'Replacement cost cannot be negative.'; return false; }
    if (!this.ratingOptions.includes(this.formData.rating)) { this.error = 'Rating must be one of: G, PG, PG-13, R, NC-17.'; return false; }
    return true;
  }

  save(): void {
    this.error = '';
    if (!this.validate()) return;
    const call = this.editItem ? this.svc.update(this.editItem.filmId, this.formData) : this.svc.create(this.formData);
    call.subscribe({
      next: () => { this.successMsg = `Film ${this.editItem ? 'updated' : 'created'}!`; this.closeModal(); this.load(); setTimeout(() => this.successMsg = '', 3000); },
      error: (e: any) => { this.error = e.error?.message || e.error?.error || e.message || 'Operation failed'; }
    });
  }

  delete(item: any): void {
    if (!confirm('Delete this Film?')) return;
    this.error = '';
    this.svc.delete(item.filmId).subscribe({
      next: () => { this.successMsg = 'Film deleted!'; this.load(); setTimeout(() => this.successMsg = '', 3000); },
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
          // API returned nothing for that ID — fall back to local name search
          this.filteredItems = this.items.filter(i =>
            JSON.stringify(i).toLowerCase().includes(trimmed.toLowerCase())
          );
          this.currentPage = 1; this.paginate(); this.loading = false; this.cdr.detectChanges();
        }
      });
    } else {
      // Text input — filter locally by name/title
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

  // Sub-routes
  subItems: any[] = []; showSubModal = false; subTitle = ''; subLoading = false; activeFilm: any = null; subType = '';
  newSubId = '';
  
  openActors(item: any): void {
    this.activeFilm = item; this.subType = 'actors'; this.subTitle = 'Actors in ' + item.title; this.showSubModal = true;
    this.loadSubItems();
  }
  openCategories(item: any): void {
    this.activeFilm = item; this.subType = 'categories'; this.subTitle = 'Categories for ' + item.title; this.showSubModal = true;
    this.loadSubItems();
  }
  loadSubItems(): void {
    this.subLoading = true; this.subItems = []; this.error = '';
    const req = this.subType === 'actors' ? this.svc.getActorsByFilm(this.activeFilm.filmId) : this.svc.getCategoriesByFilm(this.activeFilm.filmId);
    req.subscribe({
      next: (d: any[]) => { this.subItems = d; this.subLoading = false; this.cdr.detectChanges(); },
      error: (e: any) => { this.error = 'Failed to load'; this.subLoading = false; this.cdr.detectChanges(); }
    });
  }
  addSubItem(): void {
    if (!this.newSubId) return;
    const req = this.subType === 'actors' ? this.svc.addActorToFilm(this.activeFilm.filmId, +this.newSubId) : this.svc.addCategoryToFilm(this.activeFilm.filmId, +this.newSubId);
    req.subscribe({
      next: () => { this.newSubId = ''; this.loadSubItems(); },
      error: (e: any) => { this.error = e.error?.message || 'Failed to add'; this.cdr.detectChanges(); }
    });
  }
  removeSubItem(id: number): void {
    if (!confirm('Remove?')) return;
    const req = this.subType === 'actors' ? this.svc.removeActorFromFilm(this.activeFilm.filmId, id) : this.svc.removeCategoryFromFilm(this.activeFilm.filmId, id);
    req.subscribe({
      next: () => this.loadSubItems(),
      error: (e: any) => { this.error = 'Failed to remove'; this.cdr.detectChanges(); }
    });
  }
  closeSubModal(): void { this.showSubModal = false; this.activeFilm = null; this.error = ''; }

}
