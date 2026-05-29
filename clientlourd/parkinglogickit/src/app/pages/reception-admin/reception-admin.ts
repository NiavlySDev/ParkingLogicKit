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
  activeTab: string = 'accueil';
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
  ) {}

  async ngOnInit(): Promise<void> {
    // BARRIÈRE DE SÉCURITÉ 1 : Validation stricte de l'identité et du rôle Admin
    const currentRole = await this.authService.getRole();
    const authenticatedUser = await this.authService.getUsername();

    if (!authenticatedUser || currentRole !== 'Admin') {
      console.warn('Accès non autorisé au tableau de bord d’administration intercepté.');
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

              // Sécurité mathématique : Évite une division par zéro si les compteurs de la BDD sont vides
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
          console.error('Erreur lors de la lecture des compteurs du parking :', err);
        },
      });
    };

    // Premier chargement à l'initialisation
    loadParking();

    // OPTIMISATION : Fréquence calée à 15s pour préserver les performances du serveur centralisé
    this.subscription = interval(15000).subscribe(() => {
      // On s'assure que l'admin est toujours authentifié avant de requêter l'API
      if (this.username) {
        loadParking();
      }
    });
  }

  ngOnDestroy(): void {
    // Nettoyage impératif du timer pour couper les requêtes fantômes en arrière-plan
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  goHome(): void {
    this.activeTab = 'accueil';
    this.router.navigate(['/reception-admin']);
  }

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  goProfile(): void {
    this.menuOpen = false;
    this.router.navigate(['/user-profile']);
  }

  goSettings(): void {
    this.menuOpen = false;
    this.router.navigate(['/settings']);
  }

  logout(): void {
    this.menuOpen = false;
    // SÉCURITÉ : On désabonne immédiatement le timer AVANT de vider les tokens de session
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
