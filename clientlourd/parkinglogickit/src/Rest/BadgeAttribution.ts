import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { REST_API_URL } from './api.config';

export interface Badge {
  id: number;
  content?: string;
  attribue?: boolean;
  [key: string]: any;
}

@Injectable({
  providedIn: 'root',
})
export class BadgeService {
  private apiUrl: string = `${REST_API_URL}/BadgeService`;

  constructor(private http: HttpClient) {}

  public setApiUrl(baseUrl: string): void {
    this.apiUrl = `${baseUrl}/BadgeService`;
  }

  public add(badge: Badge): Observable<Badge> {
    return this.http.post<Badge>(`${this.apiUrl}/`, badge);
  }

  public remove(badge: Badge): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/`, {
      body: badge,
    });
  }

  public update(badge: Badge): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/`, badge);
  }

  public getById(id: number): Observable<Badge> {
    return this.http.get<Badge>(`${this.apiUrl}/${id}`);
  }

  public getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/Count`);
  }

  public getAll(): Observable<Badge[]> {
    return this.http.get<Badge[]>(`${this.apiUrl}/`);
  }

  public getAllPaginated(begin: number, count: number): Observable<Badge[]> {
    return this.http.get<Badge[]>(`${this.apiUrl}/${begin}/${count}`);
  }

  public getBycontent(content: string): Observable<Badge> {
    return this.http.get<Badge>(`${this.apiUrl}/getBycontent/${content}`);
  }

  public getByAttribution(attribue: boolean): Observable<Badge[]> {
    return this.http.get<Badge[]>(`${this.apiUrl}/getByAttribution/${attribue}`);
  }
}
