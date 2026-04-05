import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../../Rest/RestServer';
import { Vehicle } from '../../../../Auth/Vehicle.js';
import { Router } from '@angular/router';
import { PrimengModule } from '../../../shared/primeng.module';

@Component({
  selector: 'app-modify-vehicle',
  imports: [FormsModule, CommonModule, PrimengModule],
  templateUrl: './modify-vehicle.html',
  styleUrl: './modify-vehicle.css',
})
export class ModifyVehicle implements OnInit {

  brand: string = '';
  numberPlate: string = '';
  Type: number | null = null;

  isLoading: boolean = false;
  message: string = '';
  messageType: 'success' | 'error' = 'success';

  vehicles: any[] = [];
  selectedVehicle: any;
  showPassword: boolean = false;

  constructor(
    private restServer: RestServer,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
  ) {}

  ngOnInit() {
    this.restServer.getVehicleService().getAll().subscribe({
      next: (vehicles: any[]) => {
        this.ngZone.run(() => {
          this.vehicles = vehicles.map(d => ({ ...d, fullName: `${d.brand} | ${d.numberPlate} | ${d.type}` }));
        });
      }
    });
  }

  goHome(): void {
    this.router.navigate(['/reception-admin']);
  }

  onSubmit(): void {
    if (
      !this.selectedVehicle ||
      !this.brand ||
      !this.numberPlate ||
      this.Type === null
    ) {
      this.setMessage('Tous les champs sont obligatoires', 'error');
      return;
    }

    this.isLoading = true;
    this.message = '';

    const VehicleClass =
      this.Type === 0
        ? 'lml.snir.parkinglogickit.metier.entity.vehicle'
        : this.Type === 1
          ? 'lml.snir.parkinglogickit.metier.entity.VehicleType'
          : 'lml.snir.parkinglogickit.metier.entity.Vehicle';

    const VehicleData: any = {
      id: this.selectedVehicle.id,
      brand: this.brand,
      numberPlate: this.numberPlate,
      class: VehicleClass,
    };

    this.restServer
      .getVehicleService()
      .update(VehicleData as Vehicle)
      .subscribe({
        next: () => {
          this.ngZone.run(() => {
            this.isLoading = false;
            this.setMessage('Modification réussie ✅', 'success');
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
    this.Type = null;
    this.selectedVehicle = null;
  }

  changeVehicle(): void {
    this.ngZone.run(() => {
      if (!this.selectedVehicle) {
        this.setMessage('Veuillez sélectionner un Vehicle', 'error');
        return;
      }

      this.brand = this.selectedVehicle.brand;
      this.numberPlate = this.selectedVehicle.numberPlate;
      this.Type =
        this.selectedVehicle.class === 'lml.snir.parkinglogickit.metier.entity.vehicle'
          ? 0
          : this.selectedVehicle.class === 'lml.snir.parkinglogickit.metier.entity.VehicleType'
            ? 1
            : 2;

      this.cdr.detectChanges();
    });
  }
}