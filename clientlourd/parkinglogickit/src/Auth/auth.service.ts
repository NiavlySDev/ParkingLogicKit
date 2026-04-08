import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private timeoutId: any;
  private readonly TIMEOUT_DURATION = 60 * 1000; // 15 minutes

  constructor(private router: Router) {}

  /**
   * Stocke le token JWT ou pseudo-token sécurisé
   * @param token : token reçu du backend après login
   */
  setToken(token: string): void {
    localStorage.setItem('authToken', token);
    this.startTimeout();
  }

  /**
   * Récupère le token depuis le localStorage
   */
  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  /**
   * Retourne le username depuis le token
   */
  getUsername(): string {
    const token = this.getToken();
    if (!token) return '';
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.username ?? '';
    } catch {
      return '';
    }
  }

  /**
   * Retourne le rôle de l'utilisateur depuis le token
   */
  getRole(): 'Admin' | 'Driver' | '' {
    const token = this.getToken();
    if (!token) return '';
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.role ?? '';
    } catch {
      return '';
    }
  }

  /**
   * Vérifie si l'utilisateur est Admin
   */
  isAdmin(): boolean {
    return this.getRole() === 'Admin';
  }

  /**
   * Vérifie si l'utilisateur est connecté et si le token n'est pas expiré
   */
  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return Date.now() < (payload.exp ?? 0) * 1000;
    } catch {
      return false;
    }
  }

  /**
   * Déconnecte l'utilisateur
   */
  logout(): void {
    localStorage.removeItem('authToken');
    clearTimeout(this.timeoutId);
    this.router.navigate(['/sign-in']);
  }

  /**
   * Reset du timeout (par ex. quand l'utilisateur interagit)
   */
  resetTimeout(): void {
    clearTimeout(this.timeoutId);
    this.startTimeout();
  }

  /**
   * Timeout automatique après 15 minutes d'inactivité
   */
  private startTimeout(): void {
    this.timeoutId = setTimeout(() => {
      this.logout();
    }, this.TIMEOUT_DURATION);
  }
}