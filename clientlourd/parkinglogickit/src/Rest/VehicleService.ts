import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vehicle } from '../Auth/Vehicle';

@Injectable({
  providedIn: 'root',
})
export class VehicleService {
  private apiUrl: string = '/ParkingLogicKit/rest/VehicleService';

  private headers = new HttpHeaders({
    'X-Login': 'PLK',
    'X-Pass': 'PASSPLK',
  });

  constructor(private http: HttpClient) {}

  public setApiUrl(baseUrl: string): void {
    this.apiUrl = `${baseUrl}/VehicleService`;
  }

  public add(vehicle: Vehicle): Observable<Vehicle> {
    return this.http.post<Vehicle>(`${this.apiUrl}/`, vehicle, { headers: this.headers });
  }

  public remove(vehicle: Vehicle): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/`, {
      headers: this.headers,
      body: vehicle,
    });
  }

  public update(vehicle: Vehicle): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/`, vehicle, { headers: this.headers });
  }

  public getById(id: number): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.apiUrl}/${id}`, { headers: this.headers });
  }

  public getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/Count`, { headers: this.headers });
  }

  public getAll(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${this.apiUrl}/`, { headers: this.headers });
  }

  public getAllPaginated(begin: number, count: number): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${this.apiUrl}/${begin}/${count}`, { headers: this.headers });
  }

  public getByContent(content: string): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.apiUrl}/getByContent/${content}`, { headers: this.headers });
  }
}