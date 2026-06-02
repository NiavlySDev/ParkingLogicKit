import { CommonModule } from '@angular/common';
import { Component, signal, HostListener, OnInit, NgZone } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { PrimengModule } from './shared/primeng.module';
import { AuthService } from '../Auth/auth.service';
import { UpdateCheckService } from './services/update-check.service';
import { ThemePreference, ThemeService } from './services/theme.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, PrimengModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  protected readonly title = signal('ParkingLogicKit');
  showThemeSetup = false;

  constructor(
    private authService: AuthService,
    private updateCheckService: UpdateCheckService,
    private ngZone: NgZone,
    private themeService: ThemeService
  ) {}

  async ngOnInit(): Promise<void> {
    this.showThemeSetup = !this.themeService.hasCompletedSetup();
    await this.updateCheckService.checkOnStartup();
  }

  chooseInitialTheme(preference: ThemePreference): void {
    this.themeService.completeSetup(preference);
    this.showThemeSetup = false;
  }
  
  // Horodatage pour limiter l'execution de la reinitialisation (Throttling)
  private lastActivityChecked: number = 0;
  private readonly THROTTLE_DELAY: number = 3000; // Uniquement toutes les 3 secondes au maximum

  @HostListener('document:click')
  @HostListener('document:keypress')
  @HostListener('document:touchstart')
  onUserActivity(): void {
    const now = Date.now();

    // Si la derniere verification s'est produite il y a moins de 3 secondes, on ignore l'evenement
    if (now - this.lastActivityChecked < this.THROTTLE_DELAY) {
      return;
    }

    this.lastActivityChecked = now;

    // Execution en dehors d'Angular pour eviter de declencher des cycles de Change Detection inutiles
    this.ngZone.runOutsideAngular(async () => {
      if (await this.authService.isLoggedIn()) {
        this.authService.resetTimeout();
      }
    });
  }
}
