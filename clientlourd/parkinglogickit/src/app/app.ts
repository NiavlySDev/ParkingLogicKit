import { Component, signal, HostListener, NgZone } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { PrimengModule } from './shared/primeng.module';
import { AuthService } from '../Auth/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, PrimengModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('ParkingLogicKit');

  // Horodatage pour limiter l'execution de la reinitialisation (Throttling)
  private lastActivityChecked: number = 0;
  private readonly THROTTLE_DELAY: number = 3000; // Uniquement toutes les 3 secondes au maximum

  constructor(private authService: AuthService, private ngZone: NgZone) {}

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
