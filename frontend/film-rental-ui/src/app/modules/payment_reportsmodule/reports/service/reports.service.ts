import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ReportsService {
  private url = `${environment.apiBaseUrl}/reports`;

  constructor(private http: HttpClient) {}

  // GET /api/reports/customer-list
  getCustomerList(): Observable<any> { return this.http.get<any>(`${this.url}/customer-list`); }
  // GET /api/reports/film-list
  getFilmList(): Observable<any> { return this.http.get<any>(`${this.url}/film-list`); }
  // GET /api/reports/staff-list
  getStaffList(): Observable<any> { return this.http.get<any>(`${this.url}/staff-list`); }
  // GET /api/reports/sales-by-store
  getSalesByStore(): Observable<any> { return this.http.get<any>(`${this.url}/sales-by-store`); }
  // GET /api/reports/sales-by-category
  getSalesByCategory(): Observable<any> { return this.http.get<any>(`${this.url}/sales-by-category`); }
  // GET /api/reports/actor-info
  getActorInfo(): Observable<any> { return this.http.get<any>(`${this.url}/actor-info`); }
}
