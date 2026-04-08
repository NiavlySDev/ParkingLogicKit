import { Component, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../../Rest/RestServer';
import { Vehicle } from '../../../../Auth/Vehicle.js';
import { Router } from '@angular/router';

// author Ethan
@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './add-vehicle.html',
  styleUrl: './add-vehicle.css',
})
export class AddVehicle {
  brand: string = '';
  numberPlate: string = '';
  VehicleType: number | null = null;
  isLoading: boolean = false;
  message: string = '';
  messageType: 'success' | 'error' = 'success';

  constructor(
    private restServer: RestServer,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone
  ) {}

  goHome(): void {
    this.router.navigate(['/reception-admin']);
  }

  onSubmit(): void {
    if (!this.brand || !this.numberPlate || this.VehicleType === null) {
      this.setMessage('Tous les champs sont obligatoires', 'error');
      return;
    }

    this.isLoading = true;
    this.message = '';

    const VehicleClass =
      this.VehicleType === 0
        ? 'lml.snir.parkinglogickit.metier.entity.Vehicle'
        : 'lml.snir.parkinglogickit.metier.entity.VehicleType';

    const VehicleData: any = {
      brand: this.brand,
      numberPlate: this.numberPlate,
      VehicleType: this.VehicleType,
      class: VehicleClass,
    };

    this.restServer
      .getVehicleService()
      .add(VehicleData as Vehicle)
      .subscribe({
        next: () => {
          this.ngZone.run(() => {
            this.isLoading = false;
            this.setMessage('Inscription réussie 🎉', 'success');
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

  private resetForm(): void {
    this.brand = '';
    this.numberPlate = '';
    this.VehicleType = null;
  }
}
