import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../../Rest/RestServer';
import { Router } from '@angular/router';
import { PrimengModule } from '../../../shared/primeng.module';
import { Vehicle } from '../../../../Auth/Vehicle';
import { AuthService } from '../../../../Auth/auth.service'; // Import requis pour le contrôle d'accès
import { switchMap, from, concatMap, of } from 'rxjs';

@Component({
  selector: 'app-delete-vehicle',
  standalone: true,
  imports: [FormsModule, CommonModule, PrimengModule],
  templateUrl: './delete-vehicle.html',
  styleUrl: './delete-vehicle.css',
})
export class DeleteVehicle implements OnInit {
  brand: string = '';
  numberPlate: string = '';
  VehicleType: number | null = null;

  isLoading: boolean = false;
  message: string = '';
  messageType: 'success' | 'error' = 'success';

  Vehicles: any[] = [];
  selectedVehicle: any;
  showPassword: boolean = false;

  constructor(
    private restServer: RestServer,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    private authService: AuthService // Injection du service de sécurité
  ) {}

  async ngOnInit(): Promise<void> {
    // BARRIÈRE DE SÉCURITÉ 1 : Validation stricte des droits d'administration
    const currentRole = await this.authService.getRole();
    const currentUsername = await this.authService.getUsername();

    if (!currentUsername || currentRole !== 'Admin') {
      console.warn('Accès non autorisé intercepté sur l’écran de suppression des véhicules.');
      this.authService.logout();
      this.router.navigate(['/sign-in']);
      return;
    }

    this.loadVehicles();
  }

  private loadVehicles(): void {
    this.restServer
      .getVehicleService()
      .getAll()
      .subscribe({
        next: (Vehicles: any[]) => {
          this.ngZone.run(() => {
            this.Vehicles = (Vehicles || []).map((v) => ({
              ...v,
              fullName: `${v.brand} | ${v.numberPlate} | ${v.type}`,
            }));
            this.cdr.detectChanges();
          });
        },
        error: (err) => console.error('Erreur chargement véhicules :', err),
      });
  }

  goHome(): void {
    this.router.navigate(['/reception-admin']);
  }

  onSubmit(): void {
    if (!this.selectedVehicle || !this.brand || !this.numberPlate || this.VehicleType === null) {
      this.setMessage('Tous les champs sont obligatoires', 'error');
      return;
    }

    this.isLoading = true;
    this.message = '';

    // Nettoyage de sécurité des entrées textuelles
    const sanitizedBrand = String(this.brand)
      .trim()
      .replace(/[<>"/\\;`]/g, '');
    const sanitizedPlate = String(this.numberPlate)
      .trim()
      .toUpperCase()
      .replace(/[^A-Z0-9-]/g, '');

    const VehicleData: any = {
      id: Number(this.selectedVehicle.id), // Forçage de type primitif strict (Anti-injection d'ID)
      brand: sanitizedBrand,
      numberPlate: sanitizedPlate,
      type: this.selectedVehicle.type,
      class: 'lml.snir.parkinglogickit.metier.entity.Vehicle',
    };

    // SÉCURISATION DU FLUX HTTP : Utilisation d'un pipeline séquentiel pour éliminer les erreurs réseaux
    this.restServer
      .getAssociateService()
      .getAll()
      .pipe(
        switchMap((associates) => {
          // Filtrage robuste prenant en compte le format polymorphe (objet ou plat) de l'association
          const linked = (associates || []).filter(
            (a: any) => Number(a.vehicleId ?? a.vehicle?.id) === Number(VehicleData.id)
          );

          if (linked.length === 0) {
            return this.restServer.getVehicleService().remove(VehicleData as Vehicle);
          }

          // Traitement ordonné (concatMap) ligne par ligne pour éviter les collisions sur la base distante
          return from(linked).pipe(
            concatMap((assoc: any) => this.restServer.getAssociateService().remove(assoc)),
            // Une fois toutes les associations purgées, on exécute la suppression du véhicule physique
            switchMap(() => this.restServer.getVehicleService().remove(VehicleData as Vehicle))
          );
        })
      )
      .subscribe({
        next: () => {
          this.ngZone.run(() => {
            this.isLoading = false;
            this.setMessage('Véhicule et liens d’associations supprimés avec succès 🎉', 'success');
            this.resetForm();
            this.loadVehicles();
          });
        },
        error: (error: any) => {
          this.ngZone.run(() => {
            this.isLoading = false;
            console.error('Échec transactionnel de la suppression du véhicule :', error);
            this.setMessage(
              "Une erreur est survenue. L'opération a été sécurisée et annulée.",
              'error'
            );
            this.cdr.detectChanges();
          });
        },
      });
  }

  private setMessage(message: string, type: 'success' | 'error'): void {
    this.message = message;
    this.messageType = type;
  }

  changeVehicle(): void {
    this.ngZone.run(() => {
      if (!this.selectedVehicle) {
        this.setMessage('Veuillez sélectionner un Véhicule', 'error');
        return;
      }

      this.brand = this.selectedVehicle.brand;
      this.numberPlate = this.selectedVehicle.numberPlate;

      const vehicleTypeNames = ['Moto', 'Voiture', 'Camionnette', 'Camion'];
      const index = vehicleTypeNames.indexOf(this.selectedVehicle.type);
      this.VehicleType = index !== -1 ? index : 1;

      this.cdr.detectChanges();
    });
  }

  private resetForm(): void {
    this.brand = '';
    this.numberPlate = '';
    this.VehicleType = null;
    this.selectedVehicle = null;
    this.cdr.detectChanges();
  }
}
