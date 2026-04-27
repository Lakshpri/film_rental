import { formatBackendError } from '../../../../shared/error-utils';
import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportsService } from '../service/reports.service';
 
@Component({
  standalone: true,
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  imports: [CommonModule]
})
export class ReportsComponent {
  error = '';
  activeReport: string = '';
 
  reportCards = [
    { key: 'customers',  label: 'Customer List',    icon: '👥', description: 'All registered customers' },
    { key: 'films',      label: 'Film List',         icon: '🎬', description: 'All films in catalog' },
    { key: 'staff',      label: 'Staff List',        icon: '👔', description: 'All staff members' },
    { key: 'stores',     label: 'Sales by Store',    icon: '🏪', description: 'Revenue breakdown by store' },
    { key: 'categories', label: 'Sales by Category', icon: '🏷️', description: 'Revenue breakdown by category' },
    { key: 'actors',     label: 'Actor Info',        icon: '🎭', description: 'All actor records' },
  ];
 
  customers:  any[] = []; customersLoading  = false; customersLoaded  = false;
  films:      any[] = []; filmsLoading      = false; filmsLoaded      = false;
  staff:      any[] = []; staffLoading      = false; staffLoaded      = false;
  stores:     any[] = []; storesLoading     = false; storesLoaded     = false;
  categories: any[] = []; catsLoading       = false; catsLoaded       = false;
  actors:     any[] = []; actorsLoading     = false; actorsLoaded     = false;
 
  page: Record<string, number> = {};
  pageSize = 10;
 
  constructor(private svc: ReportsService, private cdr: ChangeDetectorRef) {}
 
  selectReport(key: string): void {
    this.activeReport = key;
    this.error = '';
    this.page[key] = 1;
    this.load(key);
  }
 
  goBack(): void {
    this.activeReport = '';
    this.error = '';
  }
 
  load(key: string): void {
    switch (key) {
      case 'customers':  this.loadCustomers();  break;
      case 'films':      this.loadFilms();       break;
      case 'staff':      this.loadStaff();       break;
      case 'stores':     this.loadStores();      break;
      case 'categories': this.loadCategories();  break;
      case 'actors':     this.loadActors();      break;
    }
  }
 
  // ── Loaders ───────────────────────────────────────────────────────
 
  loadCustomers(): void {
    this.customersLoading = true; this.customersLoaded = false; this.error = '';
    this.svc.getCustomerList().subscribe({
      next: (d: any) => {
        this.customers = d.customers || [];
        this.customersLoaded = true; this.customersLoading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => { this.error = formatBackendError(e); this.customersLoading = false; this.cdr.detectChanges(); }
    });
  }
 
  loadFilms(): void {
    this.filmsLoading = true; this.filmsLoaded = false; this.error = '';
    this.svc.getFilmList().subscribe({
      next: (d: any) => {
        this.films = d.films || [];
        this.filmsLoaded = true; this.filmsLoading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => { this.error = formatBackendError(e); this.filmsLoading = false; this.cdr.detectChanges(); }
    });
  }
 
  loadStaff(): void {
    this.staffLoading = true; this.staffLoaded = false; this.error = '';
    this.svc.getStaffList().subscribe({
      next: (d: any) => {
        this.staff = d.staff || [];
        this.staffLoaded = true; this.staffLoading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => { this.error = formatBackendError(e); this.staffLoading = false; this.cdr.detectChanges(); }
    });
  }
 
  loadStores(): void {
    this.storesLoading = true; this.storesLoaded = false; this.error = '';
    this.svc.getSalesByStore().subscribe({
      next: (d: any) => {
        this.stores = d.stores || [];
        this.storesLoaded = true; this.storesLoading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => { this.error = formatBackendError(e); this.storesLoading = false; this.cdr.detectChanges(); }
    });
  }
 
  loadCategories(): void {
    this.catsLoading = true; this.catsLoaded = false; this.error = '';
    this.svc.getSalesByCategory().subscribe({
      next: (d: any) => {
        this.categories = d.categories || [];
        this.catsLoaded = true; this.catsLoading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => { this.error = formatBackendError(e); this.catsLoading = false; this.cdr.detectChanges(); }
    });
  }
 
  loadActors(): void {
    this.actorsLoading = true; this.actorsLoaded = false; this.error = '';
    this.svc.getActorInfo().subscribe({
      next: (d: any) => {
        this.actors = d.actors || [];
        this.actorsLoaded = true; this.actorsLoading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => { this.error = formatBackendError(e); this.actorsLoading = false; this.cdr.detectChanges(); }
    });
  }
 
  // ── Pagination ─────────────────────────────────────────────────────
  getPage(key: string): number { return this.page[key] || 1; }
  setPage(key: string, p: number, total: number): void {
    const max = Math.max(1, Math.ceil(total / this.pageSize));
    if (p < 1 || p > max) return;
    this.page[key] = p;
  }
  paged(items: any[], key: string): any[] {
    const p = this.getPage(key);
    return items.slice((p - 1) * this.pageSize, p * this.pageSize);
  }
  totalPages(total: number): number { return Math.max(1, Math.ceil(total / this.pageSize)); }
  pageNums(total: number, key: string): number[] {
    const tp = this.totalPages(total);
    const cur = this.getPage(key);
    const start = Math.max(1, cur - 2);
    const end = Math.min(tp, start + 4);
    const pages: number[] = [];
    for (let i = start; i <= end; i++) pages.push(i);
    return pages;
  }
 
  getActiveLabel(): string {
    return this.reportCards.find(r => r.key === this.activeReport)?.label || '';
  }
}