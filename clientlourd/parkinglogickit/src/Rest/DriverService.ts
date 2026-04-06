import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Driver } from '../Auth/Driver';

@Injectable({
  providedIn: 'root',
})
export class DriverService {
  private apiUrl: string = '/ParkingLogicKit/rest/DriverService';

  constructor(private http: HttpClient) {}

  public setApiUrl(baseUrl: string): void {
    this.apiUrl = `${baseUrl}/DriverService`;
  }

  public add(Driver: Driver): Observable<Driver> {
    return this.http.post<Driver>(`${this.apiUrl}/`, Driver);
  }

  public remove(Driver: Driver): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/`, {
      body: Driver,
    });
  }

  public update(Driver: Driver): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/`, Driver);
  }

  public getById(id: number): Observable<Driver> {
    return this.http.get<Driver>(`${this.apiUrl}/${id}`);
  }

  public getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/Count`);
  }

  public getAll(): Observable<Driver[]> {
    return this.http.get<Driver[]>(`${this.apiUrl}/`);
  }

  public getAllPaginated(begin: number, count: number): Observable<Driver[]> {
    return this.http.get<Driver[]>(`${this.apiUrl}/${begin}/${count}`);
  }

  public getByUsername(username: string): Observable<Driver> {
    return this.http.get<Driver>(`${this.apiUrl}/getByUsername/${username}?login=PLK&pass=PASSPLK`);
  }
}