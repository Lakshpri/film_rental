import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class LanguageService {
  private url = `${environment.apiBaseUrl}/languages`;
  constructor(private http: HttpClient) {}
  getAll(): Observable<any[]> { return this.http.get<any[]>(this.url); }
  getById(id: number): Observable<any> { return this.http.get<any>(`${this.url}/${id}`); }
  create(data: any): Observable<any> { return this.http.post<any>(this.url, data); }
  update(id: number, data: any): Observable<any> { return this.http.put<any>(`${this.url}/${id}`, data); }
  delete(id: number): Observable<any> { return this.http.delete(`${this.url}/${id}`); }
}
