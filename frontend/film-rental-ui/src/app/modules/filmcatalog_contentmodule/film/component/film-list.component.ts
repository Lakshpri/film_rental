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
  modalError = ''; formErrors: { [key: string]: string } = {};

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
    this.formData = { title: '', description: '', releaseYear: null, languageId: null, originalLanguageId: null, rentalDuration: 3, rentalRate: 4.99, length: null, replacementCost: 19.99, rating: 'G', specialFeatures: '' };
    this.modalError = ''; this.formErrors = {}; this.showModal = true;
  }

  openEdit(item: any): void {
    this.editItem = item;
    this.formData = { title: item.title, description: item.description, releaseYear: item.releaseYear, languageId: item.languageId, originalLanguageId: item.originalLanguageId ?? null, rentalDuration: item.rentalDuration, rentalRate: item.rentalRate, length: item.length, replacementCost: item.replacementCost, rating: item.rating, specialFeatures: item.specialFeatures };
    this.modalError = ''; this.formErrors = {}; this.showModal = true;
  }

  closeModal(): void { this.showModal = false; this.modalError = ''; this.formErrors = {}; }

  validate(): boolean {
    this.formErrors = {};
    this.modalError = '';
    if (!this.formData.title?.trim()) { this.formErrors['title'] = 'Title is required.'; }
    if (this.formData.languageId === null || this.formData.languageId === '') { this.formErrors['languageId'] = 'Language ID is required.'; }
    else if (this.formData.languageId <= 0) { this.formErrors['languageId'] = 'Language ID must be a positive number.'; }
    if (this.formData.originalLanguageId !== null && this.formData.originalLanguageId !== '' && this.formData.originalLanguageId <= 0) {
      this.formErrors['originalLanguageId'] = 'Original Language ID must be a positive number.';
    }
    if (this.formData.releaseYear && (this.formData.releaseYear < 1888 || this.formData.releaseYear > new Date().getFullYear() + 1)) {
      this.formErrors['releaseYear'] = 'Release year is not valid.';
    }
    if (this.formData.rentalDuration < 1) { this.formErrors['rentalDuration'] = 'Rental duration must be at least 1 day.'; }
    if (this.formData.rentalRate < 0) { this.formErrors['rentalRate'] = 'Rental rate cannot be negative.'; }
    if (this.formData.length !== null && this.formData.length !== '' && this.formData.length <= 0) { this.formErrors['length'] = 'Film length must be positive.'; }
    if (this.formData.replacementCost < 0) { this.formErrors['replacementCost'] = 'Replacement cost cannot be negative.'; }
    if (!this.ratingOptions.includes(this.formData.rating) && !['G','PG','PG_13','R','NC_17'].includes(this.formData.rating)) { this.formErrors['rating'] = 'Rating must be one of: G, PG, PG-13, R, NC-17.'; }
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
    const call = this.editItem ? this.svc.update(this.editItem.filmId, this.formData) : this.svc.create(this.formData);
    call.subscribe({
      next: () => { this.successMsg = `Film ${this.editItem ? 'updated' : 'created'}!`; this.closeModal(); this.load(); setTimeout(() => this.successMsg = '', 3000); },
      error: (e: any) => { this.parseBackendError(e); this.cdr.detectChanges(); }
    });
  }

  delete(item: any): void {
    if (!confirm('Delete this Film?')) return;
    this.error = '';
    this.svc.delete(item.filmId).subscribe({
      next: () => { this.successMsg = 'Film deleted!'; this.load(); setTimeout(() => this.successMsg = '', 3000); },
      error: (e: any) => { this.error = e.error?.reason || e.error?.message || e.error?.error || 'Delete failed'; }
    });
  }

 search(term: string): void {
  this.searchTerm = term.trim();
  this.error = '';

  // ✅ Reset when empty
  if (!this.searchTerm) {
    this.filteredItems = [...this.items];
    this.currentPage = 1;
    this.paginate();
    return;
  }

  // ✅ Numeric → search by ID (API)
  if (!isNaN(Number(this.searchTerm))) {
    const id = Number(this.searchTerm);
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
        // ✅ Show backend exception
        this.error = e.error?.message || e.error?.error || e.message || 'Film not found';
        this.filteredItems = [];
        this.currentPage = 1;
        this.paginate();
        this.loading = false;
        this.cdr.detectChanges();
      }
    });

  } else {
    // ✅ Text search → filter by meaningful fields
    const lower = this.searchTerm.toLowerCase();

    this.filteredItems = this.items.filter(item =>
      item.title?.toLowerCase().includes(lower) ||
      item.description?.toLowerCase().includes(lower) ||
      item.rating?.toLowerCase().includes(lower)
    );

    this.currentPage = 1;
    this.paginate();
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
  subModalError = '';

  openActors(item: any): void {
    this.activeFilm = item; this.subType = 'actors'; this.subTitle = 'Actors in ' + item.title; this.showSubModal = true;
    this.loadSubItems();
  }
  openCategories(item: any): void {
    this.activeFilm = item; this.subType = 'categories'; this.subTitle = 'Categories for ' + item.title; this.showSubModal = true;
    this.loadSubItems();
  }
  loadSubItems(): void {
    this.subLoading = true; this.subItems = []; this.subModalError = '';
    const req = this.subType === 'actors' ? this.svc.getActorsByFilm(this.activeFilm.filmId) : this.svc.getCategoriesByFilm(this.activeFilm.filmId);
    req.subscribe({
      next: (d: any[]) => { this.subItems = d; this.subLoading = false; this.cdr.detectChanges(); },
      error: (e: any) => { this.subModalError = e.error?.message || e.error?.error || 'Failed to load'; this.subLoading = false; this.cdr.detectChanges(); }
    });
  }
  addSubItem(): void {
    if (!this.newSubId) return;
    this.subModalError = '';
    const req = this.subType === 'actors' ? this.svc.addActorToFilm(this.activeFilm.filmId, +this.newSubId) : this.svc.addCategoryToFilm(this.activeFilm.filmId, +this.newSubId);
    req.subscribe({
      next: () => { this.newSubId = ''; this.loadSubItems(); },
      error: (e: any) => { this.subModalError = e.error?.reason || e.error?.message || e.error?.error || 'Failed to add'; this.cdr.detectChanges(); }
    });
  }
  removeSubItem(id: number): void {
    if (!confirm('Remove?')) return;
    this.subModalError = '';
    const req = this.subType === 'actors' ? this.svc.removeActorFromFilm(this.activeFilm.filmId, id) : this.svc.removeCategoryFromFilm(this.activeFilm.filmId, id);
    req.subscribe({
      next: () => this.loadSubItems(),
      error: (e: any) => { this.subModalError = e.error?.reason || e.error?.message || e.error?.error || 'Failed to remove'; this.cdr.detectChanges(); }
    });
  }
  closeSubModal(): void { this.showSubModal = false; this.activeFilm = null; this.subModalError = ''; }
}
