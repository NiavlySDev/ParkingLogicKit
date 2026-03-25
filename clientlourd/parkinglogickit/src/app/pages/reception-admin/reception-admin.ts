import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PrimengModule } from '../../shared/primeng.module';

@Component({
  selector: 'app-reception-admin',
  standalone: true,
  imports: [FormsModule, CommonModule, PrimengModule, RouterLink],
  templateUrl: './reception-admin.html',
  styleUrls: ['./reception-admin.css'],
})
export class ReceptionAdmin {
  username: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router  // ← ajouté
  ) {
    this.username = this.route.snapshot.queryParamMap.get('username') ?? '';
  }

  goHome(): void {
    this.router.navigate(['/reception-admin'], {
      queryParams: { username: this.username }
    });
  }
}