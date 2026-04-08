import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { PrimengModule } from '../../shared/primeng.module';
import { AuthService } from '../../../Auth/auth.service';

@Component({
  selector: 'app-reception',
  standalone: true,
  imports: [FormsModule, CommonModule, PrimengModule],
  templateUrl: './reception.html',
  styleUrls: ['./reception.css'],
})
export class Reception {
  username: string = '';
  activeTab: string = 'dashboard';
  placesTotal: number = 60;
  placesOccupees: number = 18;
  placesLibres: number = 42;
  tauxOccupation: number = 0;
  showUserMenu: boolean = false;

  constructor(private router: Router, private authService: AuthService) {
    this.username = this.authService.getUsername();
    this.tauxOccupation = Math.round((this.placesOccupees / this.placesTotal) * 100);
  }

  toggleUserMenu(): void {
    this.showUserMenu = !this.showUserMenu;
  }

  logout(): void {
    this.showUserMenu = false;
    this.authService.logout();
  }

  goHome(): void {
    this.router.navigate(['/reception']);
  }
}