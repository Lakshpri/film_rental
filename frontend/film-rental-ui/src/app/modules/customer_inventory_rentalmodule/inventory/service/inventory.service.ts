import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class InventoryService {

  // ✅ Base URLs
  private inventoryUrl = `${environment.apiBaseUrl}/inventory`; 
  private storeUrl = `${environment.apiBaseUrl}/stores`;

  constructor(private http: HttpClient) {}

  // ================= INVENTORY =================

  // GET all inventory
  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.inventoryUrl);
  }

  // GET inventory by ID
  getById(id: number): Observable<any> {
    return this.http.get<any>(`${this.inventoryUrl}/${id}`);
  }

  // CREATE inventory
  create(data: any): Observable<any> {
    return this.http.post<any>(this.inventoryUrl, data);
  }

  // DELETE inventory
  delete(id: number): Observable<any> {
    return this.http.delete(`${this.inventoryUrl}/${id}`);
  }

  // ================= STORE INVENTORY =================

  // ✅ NEW: GET inventory by store ID
  // Endpoint: /api/stores/{storeId}/inventory
  getByStoreId(storeId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.storeUrl}/${storeId}/inventory`);
  }
}