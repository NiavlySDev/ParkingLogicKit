import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../../Rest/RestServer';
import { Router } from '@angular/router';
import { PrimengModule } from '../../../shared/primeng.module';
import { Vehicle } from '../../../../Auth/Vehicle';

@Component({
  selector: 'app-delete-vehicle',
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

  ngOnInit() {
    this.restServer
      .getVehicleService()
      .getAll()
      .subscribe({
        next: (Vehicles: any[]) => {
          this.ngZone.run(() => {
            this.Vehicles = Vehicles.map((d) => ({
              ...d,
              fullName: `${d.brand} | ${d.numberPlate} | ${d.type}`,
            }));
          });
        },
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

    const VehicleClass =
      this.VehicleType === 0
        ? 'lml.snir.parkinglogickit.metier.entity.Admin'
        : this.VehicleType === 1
        ? 'lml.snir.parkinglogickit.metier.entity.VehicleType'
        : 'lml.snir.parkinglogickit.metier.entity.Vehicle';

    const VehicleData: any = {
      id: this.selectedVehicle.id,
      brand: this.brand,
      lastName: this.numberPlate,
      class: VehicleClass,
    };

    // 1. Récupérer toutes les associations
    this.restServer
      .getAssociateService()
      .getAll()
      .subscribe({
        next: (associates) => {
          // 2. Filtrer celles liées à ce véhicule
          const linked = associates.filter((a) => a.vehicle?.id === this.selectedVehicle.id);
          console.log('Associations trouvées :', linked);
          console.log('Vehicle ID :', this.selectedVehicle.id);
          console.log('Toutes les associations :', associates);

          if (linked.length === 0) {
            // Pas d'association, on supprime directement
            this.deleteVehicle(VehicleData);
            return;
          }

          // 3. Supprimer chaque association
          let deleted = 0;
          for (const assoc of linked) {
            this.restServer
              .getAssociateService()
              .remove(assoc)
              .subscribe({
                next: () => {
                  deleted++;
                  if (deleted === linked.length) {
                    // 4. Toutes supprimées, on supprime le véhicule
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

  // Méthode extraite pour éviter la duplication
  private deleteVehicle(VehicleData: any): void {
    this.restServer
      .getVehicleService()
      .remove(VehicleData as Vehicle)
      .subscribe({
        next: () => {
          this.ngZone.run(() => {
            this.isLoading = false;
            this.setMessage('Vehicle supprimé avec succès 🎉', 'success');
            this.resetForm();
            this.cdr.detectChanges();
          });
        },
        error: (error: any) => {
          this.ngZone.run(() => {
            this.isLoading = false;
            this.setMessage(error?.error?.message || "Une erreur s'est produite", 'error');
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
        this.setMessage('Veuillez sélectionner un Vehicle', 'error');
        return;
      }

      this.brand = this.selectedVehicle.brand;
      this.numberPlate = this.selectedVehicle.numberPlate;
      this.VehicleType =
        this.selectedVehicle.class === 'lml.snir.parkinglogickit.metier.entity.vehicle'
          ? 0
          : this.selectedVehicle.class === 'lml.snir.parkinglogickit.metier.entity.Maintenance'
          ? 1
          : 2;

      this.cdr.detectChanges();
    });
  }

  private resetForm(): void {
    this.brand = '';
    this.numberPlate = '';
    this.VehicleType = null;
  }
}
