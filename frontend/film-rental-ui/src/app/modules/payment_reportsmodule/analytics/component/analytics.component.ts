import { formatBackendError } from '../../../../shared/error-utils';
import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AnalyticsService } from '../service/analytics.service';
 
@Component({
  standalone: true,
  selector: 'app-analytics',
  templateUrl: './analytics.component.html',
  imports: [CommonModule, FormsModule]
})
export class AnalyticsComponent implements OnInit {
  error = '';
  activeSection: string = '';
 
  rewardsLoading = false;
  rewardsData: any = null;
  rewardSearch = '';
  rewardsPage = 1;
  rewardsPageSize = 10;
 
  filmId = 1;
  storeId = 1;
  stockLoading = false;
  stockData: any = null;
  stockMode: 'in' | 'out' | null = null;
 
  customerId = 1;
  balanceLoading = false;
  balanceData: any = null;
 
  constructor(private svc: AnalyticsService, private cdr: ChangeDetectorRef) {}
 
  ngOnInit(): void {
    this.loadRewards();
  }
 
  loadRewards(): void {
    this.rewardsLoading = true; this.rewardsData = null; this.error = '';
    this.svc.getRewardsReport().subscribe({
      next: (d: any) => {
        this.rewardsData = {
          customers:                 d.rewardEligibleCustomers || [],
          totalCustomers:            (d.rewardEligibleCustomers || []).length,
          averageRentalsPerCustomer: d.averageRentals || 0
        };
        this.rewardsPage = 1;
        this.rewardsLoading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        this.error = formatBackendError(e);
        this.rewardsLoading = false;
        this.cdr.detectChanges();
      }
    });
  }
 
  get filteredRewards(): any[] {
    if (!this.rewardsData?.customers) return [];
    const q = this.rewardSearch.toLowerCase();
    if (!q) return this.rewardsData.customers;
    return this.rewardsData.customers.filter((c: any) =>
      (c.firstName + ' ' + c.lastName + ' ' + c.email).toLowerCase().includes(q)
    );
  }
 
  rewardsTotalPages(): number {
    return Math.max(1, Math.ceil(this.filteredRewards.length / this.rewardsPageSize));
  }
 
  rewardsPageNums(): number[] {
    const tp = this.rewardsTotalPages();
    const start = Math.max(1, this.rewardsPage - 2);
    const end = Math.min(tp, start + 4);
    const pages: number[] = [];
    for (let i = start; i <= end; i++) pages.push(i);
    return pages;
  }
 
  checkFilmInStock(): void {
    this.stockLoading = true; this.stockData = null; this.stockMode = 'in'; this.error = '';
    this.svc.getFilmInStock(this.filmId, this.storeId).subscribe({
      next: (d: any) => {
        this.stockData = d;
        this.stockLoading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        this.error = formatBackendError(e);
        this.stockLoading = false;
        this.cdr.detectChanges();
      }
    });
  }
 
  checkFilmNotInStock(): void {
    this.stockLoading = true; this.stockData = null; this.stockMode = 'out'; this.error = '';
    this.svc.getFilmNotInStock(this.filmId, this.storeId).subscribe({
      next: (d: any) => {
        this.stockData = d;
        this.stockLoading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        this.error = formatBackendError(e);
        this.stockLoading = false;
        this.cdr.detectChanges();
      }
    });
  }
 
  loadCustomerBalance(): void {
    this.balanceLoading = true; this.balanceData = null; this.error = '';
    this.svc.getCustomerBalance(this.customerId).subscribe({
      next: (d: any) => {
        this.balanceData = d;
        this.balanceLoading = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        this.error = formatBackendError(e);
        this.balanceLoading = false;
        this.cdr.detectChanges();
      }
    });
  }
}