import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Parking } from '../Auth/Parking';

@Injectable({
  providedIn: 'root',
})
export class ParkingService {
  private apiUrl: string = '/ParkingLogicKit/rest/ParkingService';

  constructor(private http: HttpClient) {}

  public setApiUrl(baseUrl: string): void {
    this.apiUrl = `${baseUrl}/ParkingService`;
  }

  public add(Parking: Parking): Observable<Parking> {
    return this.http.post<Parking>(`${this.apiUrl}/`, Parking);
  }

  public remove(Parking: Parking): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/`, {
      body: Parking,
    });
  }

  public update(Parking: Parking): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/`, Parking);
  }

  public getById(id: number): Observable<Parking> {
    return this.http.get<Parking>(`${this.apiUrl}/${id}`);
  }

  public getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/Count`);
  }

  public getAll(): Observable<Parking[]> {
    return this.http.get<Parking[]>(`${this.apiUrl}/?login=PLK&pass=PASSPLK`);
  }

  public getAllPaginated(begin: number, count: number): Observable<Parking[]> {
    return this.http.get<Parking[]>(`${this.apiUrl}/${begin}/${count}`);
  }

  /**public getByUsername(contenu: string): Observable<Parking> {
    return this.http.get<Parking>(`${this.apiUrl}/getBycontenu/${contenu}`);
  }**/
}