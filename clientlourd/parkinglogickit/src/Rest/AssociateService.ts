import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { REST_API_URL } from './api.config';

// L'interface mise à jour avec les colonnes exactes de ta base de données
export interface Associate {
  id?: number;
  driverId: number;
  vehicleId: number;
  badgeId: number;  // Aligné à 100% sur ton BADGE_ID SQL
  class?: string;   // Requis par ton backend Java/Hibernate
}

@Injectable({
  providedIn: 'root',
})
export class AssociateService {
  private apiUrl: string = `${REST_API_URL}/AssociateService`;

  constructor(private http: HttpClient) {}

  public setApiUrl(baseUrl: string): void {
    this.apiUrl = `${baseUrl}/AssociateService`;
  }

  // Cette méthode va envoyer le JSON "plat" avec driverId, vehicleId et badgeId
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
    return this.http.get<Associate[]>(`${this.apiUrl}/?login=PLK&pass=PASSPLK`);
  }
}