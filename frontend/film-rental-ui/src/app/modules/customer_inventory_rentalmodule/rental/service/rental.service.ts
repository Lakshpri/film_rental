import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class RentalService {
  private url = `${environment.apiBaseUrl}/rentals`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<any[]> { return this.http.get<any[]>(this.url); }
  getById(id: number): Observable<any> { return this.http.get<any>(`${this.url}/${id}`); }
  create(data: any): Observable<any> { return this.http.post<any>(this.url, data); }
  // Backend: PUT /api/rentals/{rentalId}/return  (return a rental — no general update)
  returnRental(rentalId: number): Observable<any> {
    return this.http.put<any>(`${this.url}/${rentalId}/return`, {});
  }
  
  // No delete on rentals — backend has no DELETE endpoint
getByCustomerId(customerId: number): Observable<any[]> {
  return this.http.get<any[]>(`${environment.apiBaseUrl}/customers/${customerId}/rentals`);
}
}
