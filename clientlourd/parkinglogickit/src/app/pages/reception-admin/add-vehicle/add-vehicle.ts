import { Component, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Vehicle } from '../../../../Auth/Vehicle';
import { Router } from '@angular/router';
import { AssociateService } from '../../../../Rest/AssociateService';

// author Ethan
@Component({
  selector: 'app-add-vehicle',
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
    private router: Router,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    private associateService: AssociateService
  ) {}

  goHome(): void {
    this.router.navigate(['/reception-admin']);
  }

  onSubmit(): void {
    if (!this.brand || !this.numberPlate || this.VehicleType === null) {
      this.setMessage('Tous les champs sont obligatoires', 'error');
      return;
    }

    const driver = JSON.parse(localStorage.getItem('driver')!);

    if (!driver) {
      this.setMessage('Aucun driver trouvé', 'error');
      return;
    }

    this.isLoading = true;
    this.message = '';

    const vehicleTypeNames = ['Moto', 'Voiture', 'Camionette', 'Camion'];

    const VehicleData: any = {
      brand: this.brand,
      numberPlate: this.numberPlate,
      type: vehicleTypeNames[this.VehicleType!],
      class: 'lml.snir.parkinglogickit.metier.entity.Vehicle',
    };

    fetch('/ParkingLogicKit/rest/VehicleService/', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(VehicleData)
    })
      .then(res => res.json())
      .then((createdVehicle) => {

        this.ngZone.run(() => {

          this.associateService.add({
            driverId: driver.id,
            vehicleId: createdVehicle.id
          }).subscribe({
            next: () => {
              this.setMessage('Driver associé au véhicule 🎯', 'success');
            },
            error: () => {
              this.setMessage('Erreur lors de l’association', 'error');
            }
          });

          localStorage.removeItem('driver');

          this.isLoading = false;
          this.setMessage('Véhicule ajouté 🎉', 'success');
          console.log('Véhicule créé:', );

          this.resetForm();
          this.cdr.detectChanges();
        });

      })
      .catch(() => {
        this.isLoading = false;
        this.setMessage("Une erreur s'est produite", 'error');
        this.cdr.detectChanges();
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