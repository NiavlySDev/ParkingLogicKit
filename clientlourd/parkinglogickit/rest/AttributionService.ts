import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Attribution {
  id: number;
  badge?: any;
  utilisateur?: any;
  dateAttribution?: Date;
  [key: string]: any;
}

@Injectable({
  providedIn: 'root',
})
export class AttributionService {
  private apiUrl: string = '/GestionTemperatureServeur/rest/AttributionService';

  constructor(private http: HttpClient) {}

  public setApiUrl(baseUrl: string): void {
    this.apiUrl = `${baseUrl}/AttributionService`;
  }

  public add(attribution: Attribution): Observable<Attribution> {
    return this.http.post<Attribution>(`${this.apiUrl}/`, attribution);
  }

  public remove(attribution: Attribution): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/`, {
      body: attribution,
    });
  }

  public update(attribution: Attribution): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/`, attribution);
  }

  public getById(id: number): Observable<Attribution> {
    return this.http.get<Attribution>(`${this.apiUrl}/${id}`);
  }

  public getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/Count`);
  }

  public getAll(): Observable<Attribution[]> {
    return this.http.get<Attribution[]>(`${this.apiUrl}/`);
  }

  public getAllPaginated(begin: number, count: number): Observable<Attribution[]> {
    return this.http.get<Attribution[]>(`${this.apiUrl}/${begin}/${count}`);
  }

  public getByBadge(idBadge: number): Observable<Attribution> {
    return this.http.get<Attribution>(`${this.apiUrl}/getByBadge/${idBadge}`);
  }

  public getByUtilisateur(idUtilisateur: number): Observable<Attribution> {
    return this.http.get<Attribution>(`${this.apiUrl}/getByUtilisateur/${idUtilisateur}`);
  }

  public isBadgeAttribue(idBadge: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/isBadgeAttribue/${idBadge}`);
  }
}
