import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Temperature {
  id: number;
  [key: string]: any;
}

export interface Mesure {
  id: number;
  local?: any;
  date?: Date;
  temperature?: number;
  [key: string]: any;
}

export interface Consigne {
  id: number;
  local?: any;
  date?: Date;
  temperatureMin?: number;
  temperatureMax?: number;
  [key: string]: any;
}

@Injectable({
  providedIn: 'root',
})
export class TemperatureService {
  private apiUrl: string = '/GestionTemperatureServeur/rest/TemperatureService';

  constructor(private http: HttpClient) {}

  public setApiUrl(baseUrl: string): void {
    this.apiUrl = `${baseUrl}/TemperatureService`;
  }

  public add(temperature: Temperature): Observable<Temperature> {
    return this.http.post<Temperature>(`${this.apiUrl}/`, temperature);
  }

  public remove(temperature: Temperature): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/`, {
      body: temperature,
    });
  }

  public update(temperature: Temperature): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/`, temperature);
  }

  public getById(id: number): Observable<Temperature> {
    return this.http.get<Temperature>(`${this.apiUrl}/${id}`);
  }

  public getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/Count`);
  }

  public getAll(): Observable<Temperature[]> {
    return this.http.get<Temperature[]>(`${this.apiUrl}/`);
  }

  public getAllPaginated(begin: number, count: number): Observable<Temperature[]> {
    return this.http.get<Temperature[]>(`${this.apiUrl}/${begin}/${count}`);
  }

  public getMesureByLocal(idLocal: number): Observable<Mesure[]> {
    return this.http.get<Mesure[]>(`${this.apiUrl}/getMesureByLocal/${idLocal}`);
  }

  public getMesureByLocalAndDate(idLocal: number, date: string): Observable<Mesure[]> {
    return this.http.get<Mesure[]>(`${this.apiUrl}/getMesureByLocalAndDate/${idLocal}/${date}`);
  }

  public getMesureByDate(date: string): Observable<Mesure[]> {
    return this.http.get<Mesure[]>(`${this.apiUrl}/getMesureByDate/${date}`);
  }

  public getConsigneByLocal(idLocal: number): Observable<Consigne[]> {
    return this.http.get<Consigne[]>(`${this.apiUrl}/getConsigneByLocal/${idLocal}`);
  }

  public getConsigneByLocalAndDate(idLocal: number, date: string): Observable<Consigne[]> {
    return this.http.get<Consigne[]>(`${this.apiUrl}/getConsigneByLocalAndDate/${idLocal}/${date}`);
  }

  public getConsigneByDate(date: string): Observable<Consigne[]> {
    return this.http.get<Consigne[]>(`${this.apiUrl}/getConsigneByDate/${date}`);
  }
}
