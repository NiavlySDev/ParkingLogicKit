import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RestServer } from '../../../Rest/RestServer';
import { AuthService } from '../../../Auth/auth.service';
import { Vehicle } from '../../../Auth/Vehicle';
import { Associate } from '../../../Rest/AssociateService';
import { PrimengModule } from '../../shared/primeng.module';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, PrimengModule],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.css'
})
export class UserProfile implements OnInit {

  menuOpen: boolean = false;
  username: string = '';
  role: string = '';
  driverId: number = 0;

  // Tous les véhicules du système
  allVehicles: any[] = [];
  // Associations du driver connecté
  associations: Associate[] = [];
  // Véhicules associés au driver (affichés)
  vehicles: any[] = [];

  brand: string = '';
  numberPlate: string = '';
  selectedType: number | null = null;
  readonly vehicleTypeNames = ['Moto', 'Voiture', 'Camionnette', 'Camion'];
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

  ngOnInit(): void {
    this.username = this.authService.getUsername() || 'Utilisateur';
    this.role = this.authService.getRole() || 'Driver';
    this.loadDriverThenVehicles();
  }

  goHome(): void {
    if (this.role === 'Admin') {
      this.router.navigate(['/reception-admin']);
    } else {
      this.router.navigate(['/reception']);
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/sign-in']);
  }

  // 1. Récupère le driverId depuis la liste des drivers, puis charge les véhicules
  loadDriverThenVehicles(): void {
    this.restServer.getDriverService().getAll().subscribe({
      next: (drivers: any[]) => {
        const driver = drivers.find(
          (d: any) => d.login === this.username || d.username === this.username || d.name === this.username
        );
        if (driver) {
          this.driverId = driver.id;
          this.loadVehiclesAndAssociations();
        } else {
          console.error('Driver introuvable pour le login :', this.username);
        }
      },
      error: (err) => console.error('Erreur récupération drivers :', err)
    });
  }


  goProfile(): void {
    this.menuOpen = false;
    this.router.navigate(['/user-profile']);
  }

  // 2. Charge tous les véhicules + toutes les associations,
  //    puis filtre ceux associés au driver connecté
  loadVehiclesAndAssociations(): void {
    this.restServer.getVehicleService().getAll().subscribe({
      next: (allVehicles: any[]) => {
        this.allVehicles = allVehicles || [];

        this.restServer.getAssociateService().getAll().subscribe({
          next: (allAssociations: Associate[]) => {
            this.ngZone.run(() => {
              // On garde uniquement les associations du driver connecté
              this.associations = (allAssociations || []).filter(
                (a: any) => a.driver?.id === this.driverId || a.driverId === this.driverId
              );

              // On reconstruit la liste des véhicules associés
              this.vehicles = this.associations
                .map((a: any) => {
                  const vehicleId = a.vehicle?.id ?? a.vehicleId;
                  return this.allVehicles.find((v) => v.id === vehicleId);
                })
                .filter((v) => v !== undefined);

              this.cdr.detectChanges();
            });
          },
          error: (err) => console.error('Erreur récupération associations :', err)
        });
      },
      error: (err) => console.error('Erreur récupération véhicules :', err)
    });
  }

  // 3. Ajoute un véhicule ET crée l'association derrière
  onSubmit(): void {
    if (!this.brand || !this.numberPlate || this.selectedType === null) {
      console.error('Tous les champs sont obligatoires');
      return;
    }

    this.isLoading = true;

    const vehicleData: any = {
      brand: this.brand,
      numberPlate: this.numberPlate,
      type: this.vehicleTypeNames[this.selectedType],
      owner: this.username,
      class: 'lml.snir.parkinglogickit.metier.entity.Vehicle'
    };

    // Étape 1 : créer le véhicule
    this.restServer.getVehicleService().add(vehicleData as Vehicle).subscribe({
      next: (createdVehicle: any) => {
        const vehicleId = createdVehicle?.id;

        if (!vehicleId) {
          console.error('Véhicule créé mais ID manquant');
          this.isLoading = false;
          return;
        }

        // Étape 2 : créer l'association driver <-> véhicule
        const association: any = {
          driver: { id: Number(this.driverId) },
          vehicle: { id: Number(vehicleId) },
          badge: { id: 1 },
          class: 'lml.snir.parkinglogickit.metier.entity.Associate'
        };

        this.restServer.getAssociateService().add(association).subscribe({
          next: () => {
            this.ngZone.run(() => {
              this.isLoading = false;
              this.brand = '';
              this.numberPlate = '';
              this.selectedType = null;
              this.loadVehiclesAndAssociations(); // Rafraîchit la liste
            });
          },
          error: (err) => {
            this.isLoading = false;
            console.error("Erreur lors de la création de l'association :", err);
          }
        });
      },
      error: (err) => {
        this.isLoading = false;
        console.error("Erreur lors de l'ajout du véhicule :", err);
      }
    });
  }
}