import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vehicle } from '../Auth/Vehicle';

@Injectable({
  providedIn: 'root',
})
export class VehicleService {
  private apiUrl: string = '/ParkingLogicKit/rest/VehicleService';
  public headers = '?login=PLK&pass=PASSPLK';

  constructor(private http: HttpClient) {}

  public setApiUrl(baseUrl: string): void {
    this.apiUrl = `${baseUrl}/VehicleService`;
  }

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
    return this.http.get<Vehicle>(`${this.apiUrl}/${id}/${this.headers}`);
  }

  public getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/Count/${this.headers}`);
  }

  public getAll(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${this.apiUrl}${this.headers}`);
  }

  public getAllPaginated(begin: number, count: number): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${this.apiUrl}/${begin}/${count}${this.headers}`);
  }

  public getByContent(content: string): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.apiUrl}/getByContent/${content}${this.headers}`);
  }
}
