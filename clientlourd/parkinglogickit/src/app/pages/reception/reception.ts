import { Component, OnInit, OnDestroy, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { PrimengModule } from '../../shared/primeng.module';
import { AuthService } from '../../../Auth/auth.service';
import { ParkingService } from '../../../Rest/ParkingService';
import { Parking } from '../../../Auth/Parking';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-reception',
  standalone: true,
  imports: [FormsModule, CommonModule, PrimengModule],
  templateUrl: './reception.html',
  styleUrls: ['./reception.css'],
})
export class Reception implements OnInit, OnDestroy {
  username: string = '';
  activeTab: string = 'dashboard';
  placesTotal: number = 0;
  placesOccupees: number = 0;
  placesLibres: number = 0;
  tauxOccupation: number = 0;
  menuOpen: boolean = false;

  private subscription: Subscription = new Subscription();

  constructor(
    private router: Router,
    private authService: AuthService,
    private parkingService: ParkingService,
    private ngZone: NgZone,
    private cdr: ChangeDetectorRef
  ) {} // On laisse le constructeur vide et propre

  // CORRECTIF : Passage en async pour pouvoir faire le await
  async ngOnInit(): Promise<void> {
    // Récupération sécurisée et asynchrone du username
    this.username = (await this.authService.getUsername()) || 'Utilisateur';

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

  toggleUserMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  goProfile(): void {
    this.menuOpen = false;
    this.router.navigate(['/user-profile']);
  }

  logout(): void {
    this.menuOpen = false;
    this.authService.logout();
    this.router.navigate(['/sign-in']);
  }

  goHome(): void {
    this.router.navigate(['/reception']);
  }
}
