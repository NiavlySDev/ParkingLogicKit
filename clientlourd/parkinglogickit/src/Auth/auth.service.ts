import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Capacitor } from '@capacitor/core';
import { SecureStoragePlugin } from 'capacitor-secure-storage-plugin';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private router = inject(Router);
  private timeoutId: any = null;
  private readonly TIMEOUT_DURATION = 10 * 60 * 1000; // 10 minutes

  // Detection automatique du support (true sur Android/iOS natif, false sur navigateur)
  private isMobile = Capacitor.isNativePlatform();

  /**
   * Stocke le token de maniere adaptee a la plateforme (Hybride)
   */
  async setToken(token: string): Promise<void> {
    try {
      if (this.isMobile) {
        // Mode Android : Coffre-fort materiel (Android Keystore via le plugin)
        await SecureStoragePlugin.set({ key: 'authToken', value: token });
      } else {
        // Mode Web : Stockage direct (Le JWT etant deja signe et chiffre par le backend Java)
        localStorage.setItem('authToken', token);
      }
      this.startTimeout();
    } catch (error) {
      console.error('Erreur lors du stockage du token :', error);
    }
  }

  /**
   * Recupere le token de maniere asynchrone selon la plateforme
   */
  async getToken(): Promise<string | null> {
    try {
      if (this.isMobile) {
        // Mode Android
        const { value } = await SecureStoragePlugin.get({ key: 'authToken' });
        return value || null;
      } else {
        // Mode Web
        return localStorage.getItem('authToken');
      }
    } catch {
      await this.purgeSession();
      return null;
    }
  }

  /**
   * Decodeur JWT robuste compatible avec l'UTF-8 (caracteres speciaux/accents)
   */
  private decodeJwtPayload(token: string): any {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        window
          .atob(base64)
          .split('')
          .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
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
   * Retourne le role de l'utilisateur depuis le token (Asynchrone)
   */
  async getRole(): Promise<'Admin' | 'Driver' | ''> {
    const token = await this.getToken();
    if (!token) return '';
    const payload = this.decodeJwtPayload(token);
    return payload?.role ?? '';
  }

  /**
   * Verifie si l'utilisateur est Admin (Asynchrone)
   */
  async isAdmin(): Promise<boolean> {
    const role = await this.getRole();
    return role === 'Admin';
  }

  /**
   * Verifie si l'utilisateur est connecte et si le token n'est pas expire (Asynchrone)
   */
  async isLoggedIn(): Promise<boolean> {
    const token = await this.getToken();
    if (!token) return false;

    const payload = this.decodeJwtPayload(token);
    if (!payload || !payload.exp) return false;

    return Date.now() < payload.exp * 1000;
  }

  /**
   * Deconnecte l'utilisateur de maniere asynchrone et securisee
   */
  async logout(): Promise<void> {
    // SÉCURISATION : Attente imperative de la destruction du token avant la redirection
    await this.purgeSession();
    this.router.navigate(['/sign-in']);
  }

  /**
   * Reset du timeout d'inactivite
   */
  resetTimeout(): void {
    this.clearCurrentTimeout();
    this.startTimeout();
  }

  /**
   * Lancement du compte a rebours d'inactivite
   */
  private startTimeout(): void {
    this.clearCurrentTimeout();
    this.timeoutId = setTimeout(async () => await this.logout(), this.TIMEOUT_DURATION);
  }

  private clearCurrentTimeout(): void {
    if (this.timeoutId) {
      clearTimeout(this.timeoutId);
      this.timeoutId = null;
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
      console.error('Erreur lors de la purge de la session :', error);
    }
  }
}
