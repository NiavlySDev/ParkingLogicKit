import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../auth/User';

@Injectable({
  providedIn: 'root',
})
export class UtilisateurService {
  private apiUrl: string = '/GestionTemperatureServeur/rest/UtilisateurService';

  constructor(private http: HttpClient) {}

  public setApiUrl(baseUrl: string): void {
    this.apiUrl = `${baseUrl}/UtilisateurService`;
  }

  public add(user: User): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/`, user);
  }

  public remove(user: User): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/`, {
      body: user,
    });
  }

  public update(user: User): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/`, user);
  }

  public getById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  public getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/Count`);
  }

  public getAll(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/`);
  }

  public getAllPaginated(begin: number, count: number): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/${begin}/${count}`);
  }

  public getByLogin(login: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/getByLogin/${login}`);
  }
}
