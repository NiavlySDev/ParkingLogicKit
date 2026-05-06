import { Component, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AssociateService } from '../../../../Rest/AssociateService';

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
  selectedVehicleType: number | null = null;
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
    if (!this.brand || !this.numberPlate || this.selectedVehicleType === null) {
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

    const VehicleData: any = {
      brand: this.brand,
      numberPlate: this.numberPlate,
      type: this.selectedVehicleType,
      class: 'lml.snir.parkinglogickit.metier.entity.Vehicle',
    };

    fetch('/ParkingLogicKit/rest/VehicleService/', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(VehicleData),
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error(`Erreur HTTP création véhicule: ${res.status}`);
        }
        return res.json();
      })
      .then((createdVehicle) => {
        if (!createdVehicle || !createdVehicle.id) {
          this.setMessage('Erreur: véhicule non valide', 'error');
          this.isLoading = false;
          return;
        }

        this.ngZone.run(() => {
          this.associateService
            .add({
              driver: { id: Number(driver.id) },
              vehicle: { id: Number(createdVehicle.id) },
              badge: { id: 1 },
              class: 'lml.snir.parkinglogickit.metier.entity.Associate',
            } as any)
            .subscribe({
              next: () => {
                this.setMessage('Driver associé au véhicule avec succès!', 'success');
                localStorage.removeItem('driver');
                this.resetForm();
                this.isLoading = false;
                this.cdr.detectChanges();
              },
              error: (err) => {
                console.error('Erreur association:', err);
                this.setMessage("Erreur lors de l'association", 'error');
                this.isLoading = false;
                this.cdr.detectChanges();
              },
            });
        });
      })
      .catch((err) => {
        console.error('Erreur création véhicule:', err);
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
    this.selectedVehicleType = null;
  }
}