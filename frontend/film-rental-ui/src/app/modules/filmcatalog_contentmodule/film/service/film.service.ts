import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class FilmService {
  private url = `${environment.apiBaseUrl}/films`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<any[]> { return this.http.get<any[]>(this.url); }
  getById(id: number): Observable<any> { return this.http.get<any>(`${this.url}/${id}`); }
  create(data: any): Observable<any> { return this.http.post<any>(this.url, data); }
  update(id: number, data: any): Observable<any> { return this.http.put<any>(`${this.url}/${id}`, data); }
  delete(id: number): Observable<any> { return this.http.delete(`${this.url}/${id}`); }
  // GET /api/films/{filmId}/actors
  getActorsByFilm(filmId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/${filmId}/actors`);
  }
  // POST /api/films/{filmId}/actors/{actorId}
  addActorToFilm(filmId: number, actorId: number): Observable<any> {
    return this.http.post<any>(`${this.url}/${filmId}/actors/${actorId}`, {});
  }
  // DELETE /api/films/{filmId}/actors/{actorId}
  removeActorFromFilm(filmId: number, actorId: number): Observable<any> {
    return this.http.delete(`${this.url}/${filmId}/actors/${actorId}`);
  }
  // GET /api/films/{filmId}/categories
  getCategoriesByFilm(filmId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/${filmId}/categories`);
  }
  // POST /api/films/{filmId}/categories/{categoryId}
  addCategoryToFilm(filmId: number, categoryId: number): Observable<any> {
    return this.http.post<any>(`${this.url}/${filmId}/categories/${categoryId}`, {});
  }
  // DELETE /api/films/{filmId}/categories/{categoryId}
  removeCategoryFromFilm(filmId: number, categoryId: number): Observable<any> {
    return this.http.delete(`${this.url}/${filmId}/categories/${categoryId}`);
  }
}
