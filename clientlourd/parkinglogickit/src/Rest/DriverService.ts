import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Driver } from '../Auth/Driver';
import { REST_API_URL } from './api.config';

@Injectable({
  providedIn: 'root',
})
export class DriverService {
  private apiUrl: string = `${REST_API_URL}/DriverService`;

  public headers = '?login=PLK&pass=PASSPLK';

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
    return this.http.get<Driver>(`${this.apiUrl}/${id}/${this.headers}`);
  }

  public getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/Count/${this.headers}`);
  }

  public getAll(): Observable<Driver[]> {
    return this.http.get<Driver[]>(`${this.apiUrl}${this.headers}`);
  }

  public getAllPaginated(begin: number, count: number): Observable<Driver[]> {
    return this.http.get<Driver[]>(`${this.apiUrl}/${begin}/${count}${this.headers}`);
  }

  public getByUsername(username: string): Observable<Driver> {
    return this.http.get<Driver>(`${this.apiUrl}/getByUsername/${username}${this.headers}`);
  }
}
