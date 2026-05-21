import { Component, OnInit, OnDestroy, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule, NgClass } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { PrimengModule } from '../../shared/primeng.module';
import { AuthService } from '../../../Auth/auth.service';
import { ParkingService } from '../../../Rest/ParkingService';
import { Parking } from '../../../Auth/Parking';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-reception-admin',
  standalone: true,
  imports: [FormsModule, CommonModule, PrimengModule, RouterLink, NgClass],
  templateUrl: './reception-admin.html',
  styleUrls: ['./reception-admin.css'],
})
export class ReceptionAdmin implements OnInit, OnDestroy {
  username: string = '';
  activeTab: string = 'dashboard';
  menuOpen: boolean = false;
  placesTotal: number = 0;
  placesOccupees: number = 0;
  placesLibres: number = 0;
  tauxOccupation: number = 0;

  private subscription: Subscription = new Subscription();

  constructor(
    private router: Router,
    private authService: AuthService,
    private parkingService: ParkingService,
    private ngZone: NgZone,
    private cdr: ChangeDetectorRef
  ) {} // Constructeur nettoyé et léger

  async ngOnInit(): Promise<void> {
    this.username = (await this.authService.getUsername()) || 'Administrateur';

    const loadParking = () => {
      this.parkingService.getAll().subscribe({
        next: (parkings: Parking[]) => {
          this.ngZone.run(() => {
            if (parkings && parkings.length > 0) {
              const parking = parkings[0];
              this.placesTotal = Number(parking.totalPlace);
              this.placesOccupees = Number(parking.placeCount);
              this.placesLibres = this.placesTotal - this.placesOccupees;
              this.tauxOccupation = Math.round((this.placesOccupees / this.placesTotal) * 100);
              this.cdr.detectChanges();
            }
          });
        },
        error: (err) => {
          console.error('Erreur chargement parking:', err);
        },
      });
    };

    loadParking();
    this.subscription = interval(5000).subscribe(() => loadParking());
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  goHome(): void {
    this.router.navigate(['/reception-admin']);
  }

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  goProfile(): void {
    this.menuOpen = false;
    this.router.navigate(['/user-profile']);
  }

  logout(): void {
    this.authService.logout();
    this.menuOpen = false;
    this.router.navigate(['/']);
  }
}
