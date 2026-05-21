import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import * as CryptoJS from 'crypto-js';
import { Capacitor } from '@capacitor/core';
import { SecureStoragePlugin } from 'capacitor-secure-storage-plugin';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private router = inject(Router);
  private timeoutId: any;
  private readonly TIMEOUT_DURATION = 10 * 60 * 1000; // 10 minutes
  private readonly secretKey = 'vfm#PGcp810zSkjPv3H2';
  
  // Détection automatique du support (true sur Android/iOS natif, false sur navigateur)
  private isMobile = Capacitor.isNativePlatform();

  /**
   * Stocke le token de manière adaptée à la plateforme (Hybride)
   */
  async setToken(token: string): Promise<void> {
    try {
      if (this.isMobile) {
        // Mode Android : Coffre-fort matériel (Android Keystore via le plugin)
        await SecureStoragePlugin.set({ key: 'authToken', value: token });
      } else {
        // Mode Web : Chiffrement logiciel avec CryptoJS dans le localStorage
        const encrypted = CryptoJS.AES.encrypt(token, this.secretKey).toString();
        localStorage.setItem('authToken', encrypted);
      }
      this.startTimeout();
    } catch (error) {
      console.error('Erreur lors du stockage du token:', error);
    }
  }

  /**
   * Récupère le token de manière asynchrone selon la plateforme
   */
  async getToken(): Promise<string | null> {
    try {
      if (this.isMobile) {
        // Mode Android
        const { value } = await SecureStoragePlugin.get({ key: 'authToken' });
        return value || null;
      } else {
        // Mode Web
        const encrypted = localStorage.getItem('authToken');
        if (!encrypted) return null;
        const bytes = CryptoJS.AES.decrypt(encrypted, this.secretKey);
        return bytes.toString(CryptoJS.enc.Utf8) || null;
      }
    } catch {
      await this.purgeSession();
      return null;
    }
  }

  /**
   * Décodeur JWT robuste compatible avec l'UTF-8 (caractères spéciaux/accents)
   */
  private decodeJwtPayload(token: string): any {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        window.atob(base64)
          .split('')
          .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      return JSON.parse(jsonPayload);
    } catch {
      return null;
    }
  }

  /**
   * Retourne le username depuis le token (Asynchrone)
   */
  async getUsername(): Promise<string> {
    const token = await this.getToken();
    if (!token) return 'Utilisateur';
    const payload = this.decodeJwtPayload(token);
    return payload?.username ?? 'Utilisateur';
  }

  /**
   * Retourne le rôle de l'utilisateur depuis le token (Asynchrone)
   */
  async getRole(): Promise<'Admin' | 'Driver' | ''> {
    const token = await this.getToken();
    if (!token) return '';
    const payload = this.decodeJwtPayload(token);
    return payload?.role ?? '';
  }

  /**
   * Vérifie si l'utilisateur est Admin (Asynchrone)
   */
  async isAdmin(): Promise<boolean> {
    const role = await this.getRole();
    return role === 'Admin';
  }

  /**
   * Vérifie si l'utilisateur est connecté et si le token n'est pas expiré (Asynchrone)
   */
  async isLoggedIn(): Promise<boolean> {
    const token = await this.getToken();
    if (!token) return false;

    const payload = this.decodeJwtPayload(token);
    if (!payload || !payload.exp) return false;

    return Date.now() < payload.exp * 1000;
  }

  /**
   * Déconnecte l'utilisateur
   */
  logout(): void {
    this.purgeSession();
    this.router.navigate(['/sign-in']);
  }

  /**
   * Reset du timeout d'inactivité
   */
  resetTimeout(): void {
    this.clearCurrentTimeout();
    this.startTimeout();
  }

  /**
   * Lancement du compte à rebours d'inactivité
   */
  private startTimeout(): void {
    this.clearCurrentTimeout();
    this.timeoutId = setTimeout(() => this.logout(), this.TIMEOUT_DURATION);
  }

  private clearCurrentTimeout(): void {
    if (this.timeoutId) {
      clearTimeout(this.timeoutId);
    }
  }

  /**
   * Nettoyage complet des traces de session (Hybride)
   */
  private async purgeSession(): Promise<void> {
    this.clearCurrentTimeout();
    try {
      if (this.isMobile) {
        await SecureStoragePlugin.remove({ key: 'authToken' });
      } else {
        localStorage.removeItem('authToken');
      }
    } catch (error) {
      console.error('Erreur lors de la purge de la session:', error);
    }
  }
}