import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class PaymentService {
  private paymentsUrl = `${environment.apiBaseUrl}/payments`;

  constructor(private http: HttpClient) {}

  // GET /api/payments
  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.paymentsUrl);
  }

  // GET /api/payments/{paymentId}
  getById(paymentId: number): Observable<any> {
    return this.http.get<any>(`${this.paymentsUrl}/${paymentId}`);
  }

  // GET /api/payments/customer/{customerId}
  getByCustomer(customerId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.paymentsUrl}/customer/${customerId}`);
  }

  // POST /api/payments
  create(data: any): Observable<any> {
    return this.http.post<any>(this.paymentsUrl, data);
  }

  // DELETE /api/payments/{paymentId}
  delete(paymentId: number): Observable<any> {
    return this.http.delete(`${this.paymentsUrl}/${paymentId}`);
  }
}