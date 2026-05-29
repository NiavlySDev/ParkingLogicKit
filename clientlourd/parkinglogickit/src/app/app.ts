import { Component, HostListener, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { PrimengModule } from './shared/primeng.module';
import { AuthService } from '../Auth/auth.service';
import { UpdateCheckService } from './services/update-check.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, PrimengModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  protected readonly title = signal('ParkingLogicKit');

  constructor(
    private authService: AuthService,
    private updateCheckService: UpdateCheckService
  ) {}

  async ngOnInit(): Promise<void> {
    await this.updateCheckService.checkOnStartup();
  }

  @HostListener('document:click')
  @HostListener('document:keypress')
  @HostListener('document:touchstart')
  async onUserActivity(): Promise<void> {
    if (await this.authService.isLoggedIn()) {
      this.authService.resetTimeout();
    }
  }
}
