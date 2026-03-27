// src/Auth/auth.service.ts
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthService {
  setUser(username: string, userClass: string): void {
    localStorage.setItem('username', username);
    localStorage.setItem('userClass', userClass);
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
  }
}
