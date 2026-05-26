import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../../Rest/RestServer';
import { Router } from '@angular/router';
import { PrimengModule } from '../../../shared/primeng.module';
import { Vehicle } from '../../../../Auth/Vehicle';

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
    private ngZone: NgZone
  ) {}

  ngOnInit(): void {
    this.loadVehicles();
  }

  /**
   * Charge et rafraîchit la liste des véhicules
   */
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

    const VehicleClass = 'lml.snir.parkinglogickit.metier.entity.Vehicle';

    const VehicleData: any = {
      id: this.selectedVehicle.id,
      brand: this.brand.trim(),
      numberPlate: this.numberPlate.trim().toUpperCase(),
      type: this.selectedVehicle.type, 
      class: VehicleClass,
    };

    // 1. Récupérer toutes les associations
    this.restServer
      .getAssociateService()
      .getAll()
      .subscribe({
        next: (associates) => {
          // 2. CORRECTION : Filtrer en utilisant le champ plat 'vehicleId'
          const linked = (associates || []).filter(
            (a) => a.vehicleId === this.selectedVehicle.id
          );

          if (linked.length === 0) {
            this.deleteVehicle(VehicleData);
            return;
          }

          // 3. Supprimer chaque association en cascade
          let deleted = 0;
          for (const assoc of linked) {
            this.restServer
              .getAssociateService()
              .remove(assoc)
              .subscribe({
                next: () => {
                  deleted++;
                  if (deleted === linked.length) {
                    this.deleteVehicle(VehicleData);
                  }
                },
                error: (error: any) => {
                  this.ngZone.run(() => {
                    this.isLoading = false;
                    this.setMessage(
                      error?.error?.message || "Erreur lors de la suppression de l'association",
                      'error'
                    );
                    this.cdr.detectChanges();
                  });
                },
              });
          }
        },
        error: (error: any) => {
          this.ngZone.run(() => {
            this.isLoading = false;
            this.setMessage(
              error?.error?.message || 'Erreur lors de la récupération des associations',
              'error'
            );
            this.cdr.detectChanges();
          });
        },
      });
  }

  private deleteVehicle(VehicleData: any): void {
    this.restServer
      .getVehicleService()
      .remove(VehicleData as Vehicle)
      .subscribe({
        next: () => {
          this.ngZone.run(() => {
            this.isLoading = false;
            this.setMessage('Véhicule supprimé avec succès 🎉', 'success');
            this.resetForm();
            this.loadVehicles();
          });
        },
        error: (error: any) => {
          this.ngZone.run(() => {
            this.isLoading = false;
            this.setMessage(
              error?.error?.message || "Une erreur s'est produite lors de la suppression",
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