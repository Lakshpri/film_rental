import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AnalyticsService {
  private url = `${environment.apiBaseUrl}/analytics`;

  constructor(private http: HttpClient) {}

  // GET /api/analytics/customer-balance/{customerId}
  getCustomerBalance(customerId: number): Observable<any> {
    return this.http.get<any>(`${this.url}/customer-balance/${customerId}`);
  }
  // GET /api/analytics/rewards-report
  getRewardsReport(): Observable<any> {
    return this.http.get<any>(`${this.url}/rewards-report`);
  }
  // GET /api/analytics/film-in-stock?filmId=&storeId=
  getFilmInStock(filmId: number, storeId: number): Observable<any> {
    return this.http.get<any>(`${this.url}/film-in-stock?filmId=${filmId}&storeId=${storeId}`);
  }
  // GET /api/analytics/film-not-in-stock?filmId=&storeId=
  getFilmNotInStock(filmId: number, storeId: number): Observable<any> {
    return this.http.get<any>(`${this.url}/film-not-in-stock?filmId=${filmId}&storeId=${storeId}`);
  }
}
