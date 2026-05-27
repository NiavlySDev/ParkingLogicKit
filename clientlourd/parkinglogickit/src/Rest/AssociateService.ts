import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { REST_API_URL } from './api.config';

export interface Associate {
  id?: number;
  driverId?: number;
  vehicleId?: number;
  badgeId: number;
  // Prise en compte optionnelle des objets structures pour s'aligner avec UserProfile et Hibernate
  driver?: { id: number };
  vehicle?: { id: number };
  badge?: { id: number };
  class?: string;
}

@Injectable({
  providedIn: 'root',
})
export class AssociateService {
  // L'URL est desormais immuable (readonly) pour empecher toute tentative de detournement de trafic
  private readonly apiUrl: string = `${REST_API_URL}/AssociateService`;

  constructor(private http: HttpClient) {}

  public add(associate: Associate): Observable<Associate> {
    return this.http.post<Associate>(`${this.apiUrl}/`, associate);
  }

  public remove(associate: Associate): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/`, { body: associate });
  }

  public update(associate: Associate): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/`, associate);
  }

  public getById(id: number): Observable<Associate> {
    return this.http.get<Associate>(`${this.apiUrl}/${id}`);
  }

  public getAll(): Observable<Associate[]> {
    // SECURISATION : Suppression des parametres de requete login/pass en clair dans l'URL.
    // L'authentification est assuree de maniere transparente par l'en-tete Bearer de l'intercepteur HTTP.
    return this.http.get<Associate[]>(`${this.apiUrl}/`);
  }
}
