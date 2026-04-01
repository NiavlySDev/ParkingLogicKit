import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private timeoutId: any;
  private readonly TIMEOUT_DURATION = 15 * 60 * 1000;

  constructor(private router: Router) {}

  setUser(username: string, userClass: string): void {
    localStorage.setItem('username', username);
    localStorage.setItem('userClass', userClass);
    this.startTimeout();
  }

  getUsername(): string {
    return localStorage.getItem('username') ?? '';
  }

  getUserClass(): string {
    return localStorage.getItem('userClass') ?? '';
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('username');
  }

  isAdmin(): boolean {
    return this.getUserClass().includes('Admin');
  }

  logout(): void {
    localStorage.clear();
    clearTimeout(this.timeoutId);
    this.router.navigate(['/']);
  }

  resetTimeout(): void {
    clearTimeout(this.timeoutId);
    this.startTimeout();
  }

  private startTimeout(): void {
    this.timeoutId = setTimeout(() => {
      this.logout();
    }, this.TIMEOUT_DURATION);
  }
}
