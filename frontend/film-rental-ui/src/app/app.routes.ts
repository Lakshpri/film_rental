import { Routes } from '@angular/router';
import { authGuard } from './auth/auth.guard';

export const routes: Routes = [
  { path: '',                                          loadComponent: () => import('./core/splash/splash.component').then(m => m.SplashComponent) },
  { path: 'login/:id',                                 loadComponent: () => import('./core/login/login.component').then(m => m.LoginComponent) },
  { path: 'module-home',                               loadComponent: () => import('./core/module-home/module-home.component').then(m => m.ModuleHomeComponent),     canActivate: [authGuard] },
  { path: 'endpoint-detail/:memberId/:entityIndex',    loadComponent: () => import('./core/endpoint-detail/endpoint-detail.component').then(m => m.EndpointDetailComponent), canActivate: [authGuard] },
  { path: 'dashboard',                                 loadComponent: () => import('./core/dashboard/dashboard.component').then(m => m.DashboardComponent),      canActivate: [authGuard] },
  { path: 'films',       loadComponent: () => import('./modules/filmcatalog_contentmodule/film/component/film-list.component').then(m => m.FilmListComponent),          canActivate: [authGuard] },
  { path: 'actors',      loadComponent: () => import('./modules/filmcatalog_contentmodule/actor/component/actor-list.component').then(m => m.ActorListComponent),       canActivate: [authGuard] },
  { path: 'film-texts',  loadComponent: () => import('./modules/filmcatalog_contentmodule/film-text/component/film-text-list.component').then(m => m.FilmTextListComponent), canActivate: [authGuard] },
  { path: 'customers',   loadComponent: () => import('./modules/customer_inventory_rentalmodule/customer/component/customer-list.component').then(m => m.CustomerListComponent),  canActivate: [authGuard] },
  { path: 'inventory',   loadComponent: () => import('./modules/customer_inventory_rentalmodule/inventory/component/inventory-list.component').then(m => m.InventoryListComponent), canActivate: [authGuard] },
  { path: 'rentals',     loadComponent: () => import('./modules/customer_inventory_rentalmodule/rental/component/rental-list.component').then(m => m.RentalListComponent),    canActivate: [authGuard] },
  { path: 'stores',      loadComponent: () => import('./modules/location_store_staffmodule/store/component/store-list.component').then(m => m.StoreListComponent),    canActivate: [authGuard] },
  { path: 'staff',       loadComponent: () => import('./modules/location_store_staffmodule/staff/component/staff-list.component').then(m => m.StaffListComponent),    canActivate: [authGuard] },
  { path: 'addresses',   loadComponent: () => import('./modules/location_store_staffmodule/address/component/address-list.component').then(m => m.AddressListComponent),  canActivate: [authGuard] },
  { path: 'categories',  loadComponent: () => import('./modules/master_datamodule/category/component/category-list.component').then(m => m.CategoryListComponent), canActivate: [authGuard] },
  { path: 'cities',      loadComponent: () => import('./modules/master_datamodule/city/component/city-list.component').then(m => m.CityListComponent),     canActivate: [authGuard] },
  { path: 'countries',   loadComponent: () => import('./modules/master_datamodule/country/component/country-list.component').then(m => m.CountryListComponent,),  canActivate: [authGuard] },
  { path: 'languages',   loadComponent: () => import('./modules/master_datamodule/language/component/language-list.component').then(m => m.LanguageListComponent), canActivate: [authGuard] },
  { path: 'payments',    loadComponent: () => import('./modules/payment_reportsmodule/payment/component/payment-list.component').then(m => m.PaymentListComponent),  canActivate: [authGuard] },
  { path: 'reports',     loadComponent: () => import('./modules/payment_reportsmodule/reports/component/reports.component').then(m => m.ReportsComponent),      canActivate: [authGuard] },
  { path: 'analytics',   loadComponent: () => import('./modules/payment_reportsmodule/analytics/component/analytics.component').then(m => m.AnalyticsComponent),   canActivate: [authGuard] },
  { path: '**', redirectTo: '' }
];
