import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Parking } from '../Auth/Parking';
import { REST_API_URL } from './api.config';

@Injectable({
  providedIn: 'root',
})
export class ParkingService {
  // L'URL est rendue immuable pour bloquer tout detournement de requete
  private readonly apiUrl: string = `${REST_API_URL}/ParkingService`;

  constructor(private http: HttpClient) {}

  public add(parking: Parking): Observable<Parking> {
    return this.http.post<Parking>(`${this.apiUrl}/`, parking);
  }

  public remove(parking: Parking): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/`, {
      body: parking,
    });
  }

  public update(parking: Parking): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/`, parking);
  }

  public getById(id: number): Observable<Parking> {
    return this.http.get<Parking>(`${this.apiUrl}/${id}`);
  }

  public getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/Count`);
  }

  public getAll(): Observable<Parking[]> {
    // SECURISATION : Suppression des identifiants en clair dans l'URL.
    // L'authentification est assuree par l'en-tete Bearer via l'intercepteur HTTP.
    return this.http.get<Parking[]>(`${this.apiUrl}/`);
  }

  public getAllPaginated(begin: number, count: number): Observable<Parking[]> {
    return this.http.get<Parking[]>(`${this.apiUrl}/${begin}/${count}`);
  }
}
