import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { environment } from '../../environments/environment';
 
export interface ApiEndpoint {
  method: 'GET' | 'POST' | 'PUT' | 'DELETE';
  path: string;
  description: string;
  route?: string;
  isSubRoute?: boolean;
  paramLabel?: string;
}
 
export interface EntityEndpoints {
  label: string;
  icon: string;
  description: string;
  route: string;
  apiBase: string;
  endpoints: ApiEndpoint[];
}
 
export interface TeamMember {
  id: string;
  displayName: string;
  username: string;
  backendUsername: string;
  role: string;
  module: string;
  color: string;
  entities: EntityEndpoints[];
}
 
@Injectable({ providedIn: 'root' })
export class AuthService {
 
  private _loggedIn = false;
  private _currentMember: TeamMember | null = null;
  private _credentials = '';
 
  readonly teamMembers: TeamMember[] = [
    {
      id: 'padmaprabha',
      displayName: 'Padmaprabha K',
      username: 'padmaprabha',
      backendUsername: 'padmaprabha',
      role: 'MASTER_DATA',
      module: 'Master Data Module',
      color: '#f87171',
      entities: [
        {
          label: 'Countries', icon: '🌍', description: 'Manage country master data',
          route: '/countries', apiBase: '/api/countries',
          endpoints: [
            { method: 'GET',    path: '/api/countries',                    description: 'Get all countries',          route: '/countries' },
            { method: 'GET',    path: '/api/countries/{countryId}',        description: 'Get country by ID',          route: '/countries' },
            { method: 'POST',   path: '/api/countries',                    description: 'Create a new country',       route: '/countries' },
            { method: 'PUT',    path: '/api/countries/{countryId}',        description: 'Update country by ID',       route: '/countries' },
            { method: 'DELETE', path: '/api/countries/{countryId}',        description: 'Delete country by ID',       route: '/countries' },
            { method: 'GET',    path: '/api/countries/{countryId}/cities', description: 'Get cities under a country', route: '/cities', isSubRoute: true },
          ]
        },
        {
          label: 'Cities', icon: '🏙️', description: 'Manage city master data',
          route: '/cities', apiBase: '/api/cities',
          endpoints: [
            { method: 'GET',    path: '/api/cities',             description: 'Get all cities',      route: '/cities' },
            { method: 'GET',    path: '/api/cities/{cityId}',    description: 'Get city by ID',      route: '/cities' },
            { method: 'POST',   path: '/api/cities',             description: 'Create a new city',   route: '/cities' },
            { method: 'PUT',    path: '/api/cities/{cityId}',    description: 'Update city by ID',   route: '/cities' },
            { method: 'DELETE', path: '/api/cities/{cityId}',    description: 'Delete city by ID',   route: '/cities' },
          ]
        },
        {
          label: 'Languages', icon: '🗣️', description: 'Manage language records',
          route: '/languages', apiBase: '/api/languages',
          endpoints: [
            { method: 'GET',    path: '/api/languages',                 description: 'Get all languages',     route: '/languages' },
            { method: 'GET',    path: '/api/languages/{languageId}',    description: 'Get language by ID',    route: '/languages' },
            { method: 'POST',   path: '/api/languages',                 description: 'Create a new language', route: '/languages' },
            { method: 'PUT',    path: '/api/languages/{languageId}',    description: 'Update language by ID', route: '/languages' },
            { method: 'DELETE', path: '/api/languages/{languageId}',    description: 'Delete language by ID', route: '/languages' },
          ]
        },
        {
          label: 'Categories', icon: '🏷️', description: 'Manage film genre categories',
          route: '/categories', apiBase: '/api/categories',
          endpoints: [
            { method: 'GET',    path: '/api/categories',                  description: 'Get all categories',    route: '/categories' },
            { method: 'GET',    path: '/api/categories/{categoryId}',     description: 'Get category by ID',    route: '/categories' },
            { method: 'POST',   path: '/api/categories',                  description: 'Create a new category', route: '/categories' },
            { method: 'PUT',    path: '/api/categories/{categoryId}',     description: 'Update category by ID', route: '/categories' },
            { method: 'DELETE', path: '/api/categories/{categoryId}',     description: 'Delete category by ID', route: '/categories' },
          ]
        },
      ]
    },
    {
      id: 'madhumita',
      displayName: 'Madhumita S',
      username: 'madhumita',
      backendUsername: 'madhumita',
      role: 'LOCATION_STAFF',
      module: 'Location & Staff Module',
      color: '#60a5fa',
      entities: [
        {
          label: 'Addresses', icon: '📍', description: 'Manage address records',
          route: '/addresses', apiBase: '/api/addresses',
          endpoints: [
            { method: 'GET',    path: '/api/addresses',                  description: 'Get all addresses',     route: '/addresses' },
            { method: 'GET',    path: '/api/addresses/{addressId}',      description: 'Get address by ID',     route: '/addresses' },
            { method: 'GET',    path: '/api/cities/{cityId}/addresses',  description: 'Get addresses by city', route: '/addresses', isSubRoute: true },
            { method: 'POST',   path: '/api/addresses',                  description: 'Create a new address',  route: '/addresses' },
            { method: 'PUT',    path: '/api/addresses/{addressId}',      description: 'Update address by ID',  route: '/addresses' },
            { method: 'DELETE', path: '/api/addresses/{addressId}',      description: 'Delete address by ID',  route: '/addresses' },
          ]
        },
        {
          label: 'Stores', icon: '🏪', description: 'Manage store locations',
          route: '/stores', apiBase: '/api/stores',
          endpoints: [
            { method: 'GET',    path: '/api/stores',               description: 'Get all stores',     route: '/stores' },
            { method: 'GET',    path: '/api/stores/{storeId}',     description: 'Get store by ID',    route: '/stores' },
            { method: 'POST',   path: '/api/stores',               description: 'Create a new store', route: '/stores' },
            { method: 'PUT',    path: '/api/stores/{storeId}',     description: 'Update store by ID', route: '/stores' },
            { method: 'DELETE', path: '/api/stores/{storeId}',     description: 'Delete store by ID', route: '/stores' },
          ]
        },
        {
          label: 'Staff', icon: '👔', description: 'Manage staff members',
          route: '/staff', apiBase: '/api/staff',
          endpoints: [
            { method: 'GET',    path: '/api/staff',                        description: 'Get all staff',             route: '/staff' },
            { method: 'GET',    path: '/api/staff/{staffId}',              description: 'Get staff by ID',           route: '/staff' },
            { method: 'GET',    path: '/api/stores/{storeId}/staff',       description: 'Get staff by store',        route: '/staff', isSubRoute: true },
            { method: 'POST',   path: '/api/staff',                        description: 'Create a new staff member', route: '/staff' },
            { method: 'PUT',    path: '/api/staff/{staffId}',              description: 'Update staff by ID',        route: '/staff' },
            { method: 'DELETE', path: '/api/staff/{staffId}',              description: 'Delete staff by ID',        route: '/staff' },
          ]
        },
      ]
    },
    {
      id: 'krishnaprakash',
      displayName: 'Krishnaprakash B',
      username: 'krishnaprakash',
      backendUsername: 'krishnaprakash',
      role: 'FILM_CATALOG',
      module: 'Film Catalog Module',
      color: '#c9b8ff',
      entities: [
        {
          label: 'Films', icon: '🎬', description: 'Manage film catalog',
          route: '/films', apiBase: '/api/films',
          endpoints: [
            { method: 'GET',    path: '/api/films',                                description: 'Get all films',           route: '/films' },
            { method: 'GET',    path: '/api/films/{filmId}',                       description: 'Get film by ID',          route: '/films' },
            { method: 'POST',   path: '/api/films',                                description: 'Create a new film',       route: '/films' },
            { method: 'PUT',    path: '/api/films/{filmId}',                       description: 'Update film by ID',       route: '/films' },
            { method: 'DELETE', path: '/api/films/{filmId}',                       description: 'Delete film by ID',       route: '/films' },
            { method: 'GET',    path: '/api/films/{filmId}/actors',                description: 'Get actors for a film',   route: '/films', isSubRoute: true },
            { method: 'POST',   path: '/api/films/{filmId}/actors/{actorId}',      description: 'Add actor to film',       route: '/films', isSubRoute: true },
            { method: 'DELETE', path: '/api/films/{filmId}/actors/{actorId}',      description: 'Remove actor from film',  route: '/films', isSubRoute: true },
            { method: 'GET',    path: '/api/films/{filmId}/categories',            description: 'Get categories for film', route: '/films', isSubRoute: true },
            { method: 'POST',   path: '/api/films/{filmId}/categories/{catId}',    description: 'Add category to film',    route: '/films', isSubRoute: true },
            { method: 'DELETE', path: '/api/films/{filmId}/categories/{catId}',    description: 'Remove category from film', route: '/films', isSubRoute: true },
          ]
        },
        {
          label: 'Actors', icon: '🎭', description: 'Manage actor records',
          route: '/actors', apiBase: '/api/actors',
          endpoints: [
            { method: 'GET',    path: '/api/actors',              description: 'Get all actors',     route: '/actors' },
            { method: 'GET',    path: '/api/actors/{actorId}',    description: 'Get actor by ID',    route: '/actors' },
            { method: 'POST',   path: '/api/actors',              description: 'Create a new actor', route: '/actors' },
            { method: 'PUT',    path: '/api/actors/{actorId}',    description: 'Update actor by ID', route: '/actors' },
            { method: 'DELETE', path: '/api/actors/{actorId}',    description: 'Delete actor by ID', route: '/actors' },
          ]
        },
        {
          label: 'Film Texts', icon: '📝', description: 'Manage film text descriptions',
          route: '/film-texts', apiBase: '/api/film-texts',
          endpoints: [
            { method: 'GET',    path: '/api/film-texts',             description: 'Get all film texts',       route: '/film-texts' },
            { method: 'GET',    path: '/api/film-texts/{filmId}',    description: 'Get film text by film ID', route: '/film-texts' },
            { method: 'POST',   path: '/api/film-texts',             description: 'Create film text',         route: '/film-texts' },
            { method: 'PUT',    path: '/api/film-texts/{filmId}',    description: 'Update film text by ID',   route: '/film-texts' },
            { method: 'DELETE', path: '/api/film-texts/{filmId}',    description: 'Delete film text by ID',   route: '/film-texts' },
          ]
        },
      ]
    },
    {
      id: 'subbalakshmi',
      displayName: 'Subbalakshmi BV',
      username: 'subbalakshmi',
      backendUsername: 'subbalakshmi',
      role: 'CUSTOMER_RENTAL',
      module: 'Customer & Rental Module',
      color: '#22c9a0',
      entities: [
        {
          label: 'Customers', icon: '👥', description: 'Manage customer records',
          route: '/customers', apiBase: '/api/customers',
          endpoints: [
            { method: 'GET',    path: '/api/customers',                  description: 'Get all customers',     route: '/customers' },
            { method: 'GET',    path: '/api/customers/{customerId}',     description: 'Get customer by ID',    route: '/customers' },
            { method: 'POST',   path: '/api/customers',                  description: 'Create a new customer', route: '/customers' },
            { method: 'PUT',    path: '/api/customers/{customerId}',     description: 'Update customer by ID', route: '/customers' },
            { method: 'DELETE', path: '/api/customers/{customerId}',     description: 'Delete customer by ID', route: '/customers' },
          ]
        },
        {
          label: 'Inventory', icon: '📦', description: 'Manage film inventory',
          route: '/inventory', apiBase: '/api/inventory',
          endpoints: [
            { method: 'GET',    path: '/api/inventory',                       description: 'Get all inventory',      route: '/inventory' },
            { method: 'GET',    path: '/api/inventory/{inventoryId}',         description: 'Get inventory by ID',    route: '/inventory' },
            { method: 'GET',    path: '/api/stores/{storeId}/inventory',      description: 'Get inventory by store', route: '/inventory', isSubRoute: true },
            { method: 'POST',   path: '/api/inventory',                       description: 'Add inventory item',     route: '/inventory' },
            { method: 'DELETE', path: '/api/inventory/{inventoryId}',         description: 'Delete inventory item',  route: '/inventory' },
          ]
        },
        {
          label: 'Rentals', icon: '📋', description: 'Manage rental transactions',
          route: '/rentals', apiBase: '/api/rentals',
          endpoints: [
            { method: 'GET',    path: '/api/rentals',                           description: 'Get all rentals',     route: '/rentals' },
            { method: 'GET',    path: '/api/rentals/{rentalId}',                description: 'Get rental by ID',    route: '/rentals' },
            { method: 'GET',    path: '/api/customers/{customerId}/rentals',    description: 'Get rentals by customer', route: '/rentals', isSubRoute: true },
            { method: 'POST',   path: '/api/rentals',                           description: 'Create a new rental', route: '/rentals' },
            { method: 'PUT',    path: '/api/rentals/{rentalId}/return',         description: 'Return a rental',     route: '/rentals' },
          ]
        },
      ]
    },
    {
      id: 'lakshmipriya',
      displayName: 'Lakshmi Priya G',
      username: 'lakshmipriya',
      backendUsername: 'lakshmipriya',  
      role: 'PAYMENT_REPORTS',
      module: 'Payment & Reports Module',
      color: '#fbbf24',
      entities: [
        {
          label: 'Payments', icon: '💳', description: 'Manage payment records',
          route: '/payments', apiBase: '/api/payments',
          endpoints: [
            { method: 'GET',    path: '/api/payments',                       description: 'Get all payments',         route: '/payments' },
            { method: 'GET',    path: '/api/payments/{paymentId}',           description: 'Get payment by ID',        route: '/payments' },
            { method: 'GET',    path: '/api/payments/customer/{customerId}', description: 'Get payments by customer', route: '/payments', isSubRoute: true },
            { method: 'POST',   path: '/api/payments',                       description: 'Create a new payment',     route: '/payments' },
            { method: 'DELETE', path: '/api/payments/{paymentId}',           description: 'Delete payment by ID',     route: '/payments' },
          ]
        },
        {
          label: 'Reports', icon: '📊', description: 'View system reports',
          route: '/reports', apiBase: '/api/reports',
          endpoints: [
            { method: 'GET', path: '/api/reports/customer-list',     description: 'Customer list report',      route: '/reports' },
            { method: 'GET', path: '/api/reports/film-list',         description: 'Film list report',          route: '/reports' },
            { method: 'GET', path: '/api/reports/staff-list',        description: 'Staff list report',         route: '/reports' },
            { method: 'GET', path: '/api/reports/sales-by-store',    description: 'Sales by store report',     route: '/reports' },
            { method: 'GET', path: '/api/reports/sales-by-category', description: 'Sales by category report',  route: '/reports' },
            { method: 'GET', path: '/api/reports/actor-info',        description: 'Actor info report',         route: '/reports' },
          ]
        },
        {
          label: 'Analytics', icon: '📈', description: 'Business analytics & stored procedures',
          route: '/analytics', apiBase: '/api/analytics',
          endpoints: [
            { method: 'GET', path: '/api/analytics/customer-balance/{customerId}', description: 'Get customer total balance',   route: '/analytics' },
            { method: 'GET', path: '/api/analytics/rewards-report',                description: 'Rewards report by customer',   route: '/analytics' },
            { method: 'GET', path: '/api/analytics/film-in-stock',                 description: 'Check film in stock at store',  route: '/analytics' },
            { method: 'GET', path: '/api/analytics/film-not-in-stock',             description: 'Check film not in stock',       route: '/analytics' },
          ]
        },
      ]
    }
  ];
 
  constructor(private http: HttpClient) {
    const savedCreds = sessionStorage.getItem('credentials');
    const savedId = sessionStorage.getItem('memberId');
    if (savedCreds && savedId) {
      const member = this.teamMembers.find(m => m.id === savedId);
      if (member) {
        this._credentials = savedCreds;
        this._currentMember = member;
        this._loggedIn = true;
      }
    }
  }
 
  getMemberById(id: string): TeamMember | undefined {
    return this.teamMembers.find(m => m.id === id);
  }
 
  login(username: string, password: string): Observable<boolean> {
    const member = this.teamMembers.find(m => m.username === username);
    if (!member || password !== '1234') return of(false);
    this._credentials = btoa(`${member.backendUsername}:${password}`);
    this._loggedIn = true;
    this._currentMember = member;
    sessionStorage.setItem('credentials', this._credentials);
    sessionStorage.setItem('memberId', member.id);
    return of(true);
  }
 
  logout(): void {
    this._loggedIn = false;
    this._currentMember = null;
    this._credentials = '';
    sessionStorage.removeItem('credentials');
    sessionStorage.removeItem('memberId');
  }
 
  get isLoggedIn(): boolean { return this._loggedIn; }
  get currentMember(): TeamMember | null { return this._currentMember; }
  get credentials(): string { return this._credentials; }
}