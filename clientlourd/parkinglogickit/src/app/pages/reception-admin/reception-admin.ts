import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule, NgClass } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { PrimengModule } from '../../shared/primeng.module';
import { AuthService } from '../../../Auth/auth.service';

@Component({
  selector: 'app-reception-admin',
  standalone: true,
  imports: [FormsModule, CommonModule, PrimengModule, RouterLink, NgClass],
  templateUrl: './reception-admin.html',
  styleUrls: ['./reception-admin.css'],
})
export class ReceptionAdmin {
  username: string = '';
  activeTab: string = 'dashboard';
  menuOpen: boolean = false;
  placesTotal: number = 60;
  placesOccupees: number = 18;
  placesLibres: number = 42;
  tauxOccupation: number = 0;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {
    this.username = this.authService.getUsername();
    this.tauxOccupation = Math.round((this.placesOccupees / this.placesTotal) * 100);
  }

  goHome(): void {
    this.router.navigate(['/reception-admin']);
  }

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  goProfile(): void {
    this.menuOpen = false;
  }

  logout(): void {
    this.authService.logout();
    this.menuOpen = false;
    this.router.navigate(['/']);
  }
}