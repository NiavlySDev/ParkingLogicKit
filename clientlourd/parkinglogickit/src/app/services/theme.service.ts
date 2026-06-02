import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export type ThemePreference = 'light' | 'dark' | 'system';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly preferenceKey = 'plk_theme_preference';
  private readonly setupKey = 'plk_theme_setup_done';
  private readonly mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
  private readonly preferenceSubject = new BehaviorSubject<ThemePreference>(this.getPreference());

  readonly preference$ = this.preferenceSubject.asObservable();

  constructor() {
    this.applyTheme(this.preferenceSubject.value);
    this.mediaQuery.addEventListener('change', () => {
      if (this.preferenceSubject.value === 'system') {
        this.applyTheme('system');
      }
    });
  }

  getPreference(): ThemePreference {
    const stored = localStorage.getItem(this.preferenceKey);
    return stored === 'light' || stored === 'dark' || stored === 'system' ? stored : 'system';
  }

  setPreference(preference: ThemePreference): void {
    localStorage.setItem(this.preferenceKey, preference);
    localStorage.setItem(this.setupKey, 'true');
    this.preferenceSubject.next(preference);
    this.applyTheme(preference);
  }

  hasCompletedSetup(): boolean {
    return localStorage.getItem(this.setupKey) === 'true';
  }

  completeSetup(preference: ThemePreference): void {
    this.setPreference(preference);
  }

  private applyTheme(preference: ThemePreference): void {
    const useDarkTheme = preference === 'dark' || (preference === 'system' && this.mediaQuery.matches);
    document.documentElement.classList.toggle('app-theme-dark', useDarkTheme);
    document.documentElement.classList.toggle('app-theme-light', !useDarkTheme);
    document.body.classList.toggle('app-theme-dark', useDarkTheme);
    document.body.classList.toggle('app-theme-light', !useDarkTheme);
    document.documentElement.style.colorScheme = useDarkTheme ? 'dark' : 'light';
  }
}
