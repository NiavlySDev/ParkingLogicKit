import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vehicle } from '../Auth/Vehicle';

@Injectable({
  providedIn: 'root',
})
export class VehicleService {
  private apiUrl: string = '/ParkingLogicKit/rest/VehicleService';

  constructor(private http: HttpClient) {}

  public setApiUrl(baseUrl: string): void {
    this.apiUrl = `${baseUrl}/VehicleService`;
  }

  public add(Vehicle: Vehicle): Observable<Vehicle> {
    return this.http.post<Vehicle>(`${this.apiUrl}/`, Vehicle);
  }

  public remove(Vehicle: Vehicle): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/`, {
      body: Vehicle,
    });
  }

  public update(Vehicle: Vehicle): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/`, Vehicle);
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

  public getByContent(content: string): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.apiUrl}/getByUsername/${content}`);
  }
}