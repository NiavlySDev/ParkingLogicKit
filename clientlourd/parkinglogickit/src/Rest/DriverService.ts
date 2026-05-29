import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Driver } from '../Auth/Driver';
import { REST_API_URL } from './api.config';

@Injectable({
  providedIn: 'root',
})
export class DriverService {
  // SÉCURISATION : L'URL de base est immuable pour bloquer les redirections malveillantes
  private readonly apiUrl: string = `${REST_API_URL}/DriverService`;

  private readonly headers: string = '?login=PLK&pass=PASSPLK';

  constructor(private http: HttpClient) {}

  public add(driver: Driver): Observable<Driver> {
    return this.http.post<Driver>(`${this.apiUrl}/`, driver);
  }

  public remove(driver: Driver): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/`, {
      body: driver,
    });
  }

  public update(driver: Driver): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/`, driver);
  }

  public getById(id: number): Observable<Driver> {
    return this.http.get<Driver>(`${this.apiUrl}/${id}${this.headers}`);
  }

  public getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/Count${this.headers}`);
  }

  public getAll(): Observable<Driver[]> {
    return this.http.get<Driver[]>(`${this.apiUrl}/${this.headers}`);
  }

  public getAllPaginated(begin: number, count: number): Observable<Driver[]> {
    return this.http.get<Driver[]>(`${this.apiUrl}/${begin}/${count}${this.headers}`);
  }

  public getByUsername(username: string): Observable<Driver> {
    return this.http.get<Driver>(`${this.apiUrl}/getByUsername/${username}${this.headers}`);
  }
}
