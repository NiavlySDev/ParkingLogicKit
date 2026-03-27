import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule, NgClass } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PrimengModule } from '../../shared/primeng.module';

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
  placesTotal: number = 60;
  placesOccupees: number = 18;
  placesLibres: number = 42;
  tauxOccupation: number = 0;

  constructor(private route: ActivatedRoute, private router: Router) {
    this.username = this.route.snapshot.queryParamMap.get('username') ?? '';
    this.tauxOccupation = Math.round((this.placesOccupees / this.placesTotal) * 100);
  }

  goHome(): void {
    this.router.navigate(['/reception-admin'], {
      queryParams: { username: this.username },
    });
  }
}
