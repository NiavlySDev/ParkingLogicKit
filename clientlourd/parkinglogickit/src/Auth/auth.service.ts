import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import * as CryptoJS from 'crypto-js';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private timeoutId: any;
  private readonly TIMEOUT_DURATION = 10 * 60 * 1000;
  private Key = 'vfm#PGcp810zSkjPv3H2';

  constructor(private router: Router) {}

  /**
   * Stocke le token JWT chiffré
   */
  setToken(token: string): void {
    const encrypted = CryptoJS.AES.encrypt(token, this.Key).toString();
    localStorage.setItem('authToken', encrypted);
    this.startTimeout();
  }

  /**
   * Récupère et déchiffre le token
   */
  getToken(): string | null {
    const encrypted = localStorage.getItem('authToken');
    if (!encrypted) return null;

    try {
      const bytes = CryptoJS.AES.decrypt(encrypted, this.Key);
      const decrypted = bytes.toString(CryptoJS.enc.Utf8);
      return decrypted || null;
    } catch {
      return null;
    }
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
   * Reset du timeout
   */
  resetTimeout(): void {
    clearTimeout(this.timeoutId);
    this.startTimeout();
  }

  /**
   * Timeout automatique après inactivité
   */
  private startTimeout(): void {
    this.timeoutId = setTimeout(() => {
      this.logout();
    }, this.TIMEOUT_DURATION);
  }
}
