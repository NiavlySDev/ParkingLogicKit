import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RestServer } from '../../../Rest/RestServer';
import { AuthService } from '../../../Auth/auth.service';
import { Vehicle } from '../../../Auth/Vehicle';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.css'
})
export class UserProfile implements OnInit {
  username: string = '';
  role: string = '';
  vehicles: any[] = [];
  brand: string = '';
  numberPlate: string = '';
  selectedType: number = 1;
  isLoading: boolean = false;
  message: string = '';
  messageType: string = 'success';

  constructor(
    private authService: AuthService,
    private restServer: RestServer,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    private router: Router
  ) {}

  ngOnInit() {
    this.username = this.authService.getUsername() || 'Utilisateur';
    this.role = this.authService.getRole() || 'Driver';
    this.loadVehicles();
  }

  // Pour le bouton "Accueil"
  goHome(): void {
    if (this.role === 'Admin') {
      this.router.navigate(['/reception-admin']);
    } else {
      this.router.navigate(['/reception']);
    }
  }

  loadVehicles(): void {
    this.restServer.getVehicleService().getAll().subscribe({
      next: (data: any[]) => {
        this.ngZone.run(() => {
          // On filtre pour n'afficher que les véhicules de l'utilisateur connecté
          this.vehicles = (data || []).filter((v: any) => v.owner === this.username);
          this.cdr.detectChanges();
        });
      },
      error: (err) => console.error("Erreur lors de la récupération :", err)
    });
  }

  // Pour le formulaire d'ajout
  onSubmit(): void {
    if (this.vehicles.length >= 2) {
      alert("Limite de 2 véhicules atteinte.");
      return;
    }

    this.isLoading = true;
    const vehicleData: any = {
      brand: this.brand,
      numberPlate: this.numberPlate,
      type: this.selectedType === 0 ? 'Moto' : 'Voiture',
      owner: this.username,
      class: 'lml.snir.parkinglogickit.metier.entity.Vehicle'
    };

    this.restServer.getVehicleService().add(vehicleData as Vehicle).subscribe({
      next: () => {
        this.ngZone.run(() => {
          this.isLoading = false;
          this.brand = '';
          this.numberPlate = '';
          this.loadVehicles(); // On rafraîchit la liste
        });
      },
      error: (err) => {
        this.isLoading = false;
        console.error("Erreur lors de l'ajout :", err);
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/sign-in']);
  }
}