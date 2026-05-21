import { Component, signal, HostListener } from '@angular/core';
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

  constructor(private authService: AuthService) {}

  @HostListener('document:click')
  @HostListener('document:keypress')
  @HostListener('document:touchstart')
  async onUserActivity(): Promise<void> {
    if (await this.authService.isLoggedIn()) {
      this.authService.resetTimeout();
    }
  }
}