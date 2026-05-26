import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../../Rest/RestServer';
import { Router } from '@angular/router';
import { PrimengModule } from '../../../shared/primeng.module';
import { Vehicle } from '../../../../Auth/Vehicle';

@Component({
  selector: 'app-modify-vehicle',
  standalone: true,
  imports: [FormsModule, CommonModule, PrimengModule],
  templateUrl: './modify-vehicle.html',
  styleUrl: './modify-vehicle.css',
})
export class ModifyVehicle implements OnInit {
  // Champs du formulaire
  brand: string = '';
  numberPlate: string = '';
  selectedVehicleType: number | null = null;

  isLoading: boolean = false;
  message: string = '';
  messageType: 'success' | 'error' = 'success';

  // Données pour PrimeNG
  vehicles: any[] = [];
  selectedVehicle: any;

  constructor(
    private restServer: RestServer,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone
  ) {}

  ngOnInit(): void {
    this.loadVehicles();
  }

  loadVehicles(): void {
    this.restServer
      .getVehicleService()
      .getAll()
      .subscribe({
        next: (data: any[]) => {
          this.ngZone.run(() => {
            this.vehicles = (data || []).map((v) => ({
              ...v,
              fullName: `${v.brand} | ${v.numberPlate} | ${v.type}`,
            }));
            this.cdr.detectChanges();
          });
        },
        error: (err) => {
          console.error('Erreur lors du chargement des véhicules', err);
        },
      });
  }

  changeVehicle(): void {
    this.ngZone.run(() => {
      if (!this.selectedVehicle) {
        this.resetForm();
        return;
      }

      this.brand = this.selectedVehicle.brand;
      this.numberPlate = this.selectedVehicle.numberPlate;

      // Moto = 0, Voiture = 1, Camionnette = 2, Camion = 3
      const typeList = ['Moto', 'Voiture', 'Camionnette', 'Camion'];
      const index = typeList.indexOf(this.selectedVehicle.type);
      this.selectedVehicleType = index !== -1 ? index : 1;

      this.cdr.detectChanges();
    });
  }

  onSubmit(): void {
    if (
      !this.selectedVehicle ||
      !this.brand ||
      !this.numberPlate ||
      this.selectedVehicleType === null
    ) {
      this.setMessage('Tous les champs sont obligatoires', 'error');
      return;
    }

    this.isLoading = true;
    this.message = '';

    const javaClassName = 'lml.snir.parkinglogickit.metier.entity.Vehicle';

    // OPTIMISATION : Assainissement des chaînes pour éviter les doublons ou formats invalides
    const vehicleToUpdate: any = {
      id: this.selectedVehicle.id,
      brand: this.brand.trim(),
      numberPlate: this.numberPlate.trim().toUpperCase(),
      type: this.getVehicleTypeName(this.selectedVehicleType),
      class: javaClassName,
    };

    this.restServer
      .getVehicleService()
      .update(vehicleToUpdate as Vehicle)
      .subscribe({
        next: () => {
          this.ngZone.run(() => {
            this.isLoading = false;
            this.setMessage('Modification effectuée avec succès ✅', 'success');
            this.resetForm(); // OPTIMISATION : Remet à zéro l'affichage après traitement
            this.loadVehicles();
          });
        },
        error: (error: any) => {
          this.ngZone.run(() => {
            this.isLoading = false;
            console.error('Erreur update:', error);
            this.setMessage('Erreur serveur lors de la mise à jour', 'error');
            this.cdr.detectChanges();
          });
        },
      });
  }

  private getVehicleTypeName(index: number): string {
    const types = ['Moto', 'Voiture', 'Camionnette', 'Camion'];
    return types[index] || 'Voiture';
  }

  private setMessage(message: string, type: 'success' | 'error'): void {
    this.message = message;
    this.messageType = type;
  }

  private resetForm(): void {
    this.brand = '';
    this.numberPlate = '';
    this.selectedVehicleType = null;
    this.selectedVehicle = null;
    this.cdr.detectChanges();
  }

  goHome(): void {
    this.router.navigate(['/reception-admin']);
  }
}
