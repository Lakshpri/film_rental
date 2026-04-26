import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class StaffService {
  private base = '/api/staff';

  constructor(private http: HttpClient) {}

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.base);
  }

  getById(id: number): Observable<any> {
    return this.http.get<any>(`${this.base}/${id}`);
  }

  // FIX 13: createMultipart — sends FormData to POST /api/staff (multipart/form-data)
  // Do NOT set Content-Type header — HttpClient sets it automatically with the boundary.
  createMultipart(fd: FormData): Observable<any> {
    return this.http.post<any>(this.base, fd);
  }

  // FIX 14: updateMultipart — sends FormData to PUT /api/staff/{id} (multipart/form-data)
  updateMultipart(id: number, fd: FormData): Observable<any> {
    return this.http.put<any>(`${this.base}/${id}`, fd);
  }

  delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.base}/${id}`);
  }
}