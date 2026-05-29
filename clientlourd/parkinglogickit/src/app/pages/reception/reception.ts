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
  ) {}

  async ngOnInit(): Promise<void> {
    // BARRIÈRE DE SÉCURITÉ 1 : Vérification stricte de la session
    const authenticatedUser = await this.authService.getUsername();

    if (!authenticatedUser) {
      // Si aucun utilisateur n'est trouvé en session, rejet et redirection immédiate
      this.logout();
      return;
    }

    this.username = authenticatedUser;

    const loadParking = () => {
      this.parkingService.getAll().subscribe({
        next: (parkings: Parking[]) => {
          this.ngZone.run(() => {
            if (parkings && parkings.length > 0) {
              const parking = parkings[0];
              this.placesTotal = Number(parking.totalPlace) || 0;
              this.placesOccupees = Number(parking.placeCount) || 0;

              // Sécurité mathématique : Évite les divisions par zéro si la BDD est mal initialisée
              if (this.placesTotal > 0) {
                this.placesLibres = this.placesTotal - this.placesOccupees;
                this.tauxOccupation = Math.round((this.placesOccupees / this.placesTotal) * 100);
              } else {
                this.placesLibres = 0;
                this.tauxOccupation = 0;
              }

              this.cdr.detectChanges();
            }
          });
        },
        error: (err) => {
          console.error('Erreur de lecture du tableau de bord :', err);
        },
      });
    };

    // Premier chargement direct
    loadParking();

    // OPTIMISATION : Fréquence de rafraîchissement ajustée à 15s pour préserver la base centralisée
    this.subscription = interval(15000).subscribe(() => {
      // On re-vérifie rapidement que l'utilisateur n'a pas nettoyé son localStorage en cours de route
      if (this.username) {
        loadParking();
      }
    });
  }

  ngOnDestroy(): void {
    // Nettoyage impératif du timer pour couper les requêtes HTTP en arrière-plan
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
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
    this.subscription.unsubscribe(); // Sécurité : On coupe le timer AVANT de vider le token
    this.authService.logout();
    this.router.navigate(['/sign-in']);
  }

  goHome(): void {
    this.router.navigate(['/reception']);
  }
}
