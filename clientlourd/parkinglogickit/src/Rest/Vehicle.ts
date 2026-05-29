import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vehicle } from '../Auth/Vehicle';
import { REST_API_URL } from './api.config';

@Injectable({
  providedIn: 'root',
})
export class VehicleService {
  // SÉCURISATION : L'URL est rendue immuable pour bloquer toute tentative de détournement de trafic
  private readonly apiUrl: string = `${REST_API_URL}/VehicleService`;

  constructor(private http: HttpClient) {}

  public add(vehicle: Vehicle): Observable<Vehicle> {
    return this.http.post<Vehicle>(`${this.apiUrl}/`, vehicle);
  }

  public remove(vehicle: Vehicle): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/`, {
      body: vehicle,
    });
  }

  public update(vehicle: Vehicle): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/`, vehicle);
  }

  public getById(id: number): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.apiUrl}/${id}`);
  }

  public getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/Count`);
  }

  public getAll(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${this.apiUrl}/`);
  }

  public getAllPaginated(begin: number, count: number): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${this.apiUrl}/${begin}/${count}`);
  }

  public getByContent(numberPlate: string): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.apiUrl}/getByContent/${numberPlate}`);
  }
}
