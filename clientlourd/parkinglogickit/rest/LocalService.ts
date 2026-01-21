import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Local {
  id: number;
  name?: string;
  [key: string]: any;
}

@Injectable({
  providedIn: 'root',
})
export class LocalService {
  private apiUrl: string = '/GestionTemperatureServeur/rest/LocalService';

  constructor(private http: HttpClient) {}

  public setApiUrl(baseUrl: string): void {
    this.apiUrl = `${baseUrl}/LocalService`;
  }

  public add(local: Local): Observable<Local> {
    return this.http.post<Local>(`${this.apiUrl}/`, local);
  }

  public remove(local: Local): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/`, {
      body: local,
    });
  }

  public update(local: Local): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/`, local);
  }

  public getById(id: number): Observable<Local> {
    return this.http.get<Local>(`${this.apiUrl}/${id}`);
  }

  public getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/Count`);
  }

  public getAll(): Observable<Local[]> {
    return this.http.get<Local[]>(`${this.apiUrl}/`);
  }

  public getAllPaginated(begin: number, count: number): Observable<Local[]> {
    return this.http.get<Local[]>(`${this.apiUrl}/${begin}/${count}`);
  }
}
