import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule, NgClass } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PrimengModule } from '../../shared/primeng.module';

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

  constructor(private route: ActivatedRoute, private router: Router) {
    this.username = this.route.snapshot.queryParamMap.get('username') ?? '';
    this.tauxOccupation = Math.round((this.placesOccupees / this.placesTotal) * 100);
  }

  toggleUserMenu(): void {
    this.showUserMenu = !this.showUserMenu;
  }

  logout(): void {
    this.showUserMenu = false;
    this.router.navigate(['/sign-in']);
  }

  goHome(): void {
    this.router.navigate(['/reception'], {
      queryParams: { username: this.username },
    });
  }
}

