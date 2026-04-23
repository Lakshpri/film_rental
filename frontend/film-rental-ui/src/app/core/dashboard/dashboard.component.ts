import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  standalone: true,
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  imports: [CommonModule, RouterLink]
})
export class DashboardComponent implements OnInit {
  totalFilms: number | null = null;
  totalCustomers: number | null = null;
  totalActors: number | null = null;
  totalStores: number | null = null;
  totalRentals: number | null = null;
  totalPayments: number | null = null;
  totalInventory: number | null = null;
  totalStaff: number | null = null;
  totalCategories: number | null = null;
  totalLanguages: number | null = null;
  backendOnline = false;

  topCustomers: any[] = [];
  recentRentals: any[] = [];
  recentPayments: any[] = [];
  topFilms: any[] = [];

  private api = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.load('/films',      (d: any[]) => { this.totalFilms = d.length; this.backendOnline = true; this.topFilms = d.slice(0, 5); });
    this.load('/customers',  (d: any[]) => { this.totalCustomers = d.length; this.topCustomers = d.slice(0, 5); });
    this.load('/actors',     (d: any[]) => this.totalActors = d.length);
    this.load('/stores',     (d: any[]) => this.totalStores = d.length);
    this.load('/rentals',    (d: any[]) => { this.totalRentals = d.length; this.recentRentals = d.slice(-5).reverse(); });
    this.load('/payments',   (d: any[]) => { this.totalPayments = d.length; this.recentPayments = d.slice(-5).reverse(); });
    this.load('/inventory',  (d: any[]) => this.totalInventory = d.length);
    this.load('/staff',      (d: any[]) => this.totalStaff = d.length);
    this.load('/categories', (d: any[]) => this.totalCategories = d.length);
    this.load('/languages',  (d: any[]) => this.totalLanguages = d.length);
  }

  private load(path: string, cb: (d: any[]) => void): void {
    this.http.get<any[]>(`${this.api}${path}`).subscribe({ next: cb, error: () => {} });
  }

  get totalRevenue(): string {
    return this.recentPayments.length ? 'Live' : '...';
  }
}
