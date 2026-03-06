import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../Rest/RestServer';
import { Driver } from '../../../Auth/Driver.js';
import { Router } from '@angular/router';

// author Ethan

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './sign-up.html',
  styleUrl: './sign-up.css',
})
export class SignUp {
  firstname: string = '';
  lastName: string = '';
  username: string = '';
  password: string = '';
  age: number | null = null;
  isMale: boolean | null = null;
  DriverType: number | null = null;
  addCar: VehicleType | null = null;
  VehicleType = VehicleType; // exposé au HTML

  isLoading: boolean = false;
  message: string = '';
  messageType: 'success' | 'error' = 'success';

  constructor(private restServer: RestServer, private router: Router) {}

  goHome(): void {
    this.router.navigate(['/']); // redirige vers Home
  }
  onSubmit(): void {
    if (
      !this.firstname ||
      !this.lastName ||
      !this.username ||
      !this.password ||
      this.age === null ||
      this.isMale === null ||
      this.DriverType === null ||
      this.addCar === null
    ) {
      this.setMessage('Tous les champs sont obligatoires', 'error');
      return;
    }

    if (this.age < 1 || this.age > 120) {
      this.setMessage("L'âge doit être compris entre 1 et 120 ans", 'error');
      return;
    }

    this.isLoading = true;
    this.message = '';

    const DriverClass =
      this.DriverType === 0
        ? 'lml.snir.parkinglogickit.metier.entity.Admin'
        : this.DriverType === 1
        ? 'lml.snir.parkinglogickit.metier.entity.Maintenance'
        : 'lml.snir.parkinglogickit.metier.entity.Driver';
    const DriverData: any = {
      firstName: this.firstname,
      lastName: this.lastName,
      username: this.username,
      password: this.password,
      age: this.age,
      isMale: this.isMale,
      vehicleType: this.addCar,
      class: DriverClass,
    };

    console.log('JSON envoyé :', DriverData);

    this.restServer
      .getDriverService()
      .add(DriverData as Driver)
      .subscribe({
        next: () => {
          this.isLoading = false;
          this.setMessage('Inscription réussie 🎉', 'success');
          this.resetForm();
        },
        error: (error: any) => {
          this.isLoading = false;
          this.setMessage(error?.error?.message || "Une erreur s'est produite", 'error');
        },
      });
  }

  private setMessage(message: string, type: 'success' | 'error'): void {
    this.message = message;
    this.messageType = type;
  }

  private resetForm(): void {
    this.firstname = '';
    this.lastName = '';
    this.username = '';
    this.password = '';
    this.age = null;
    this.isMale = null;
    this.DriverType = null;
    this.addCar = null;
  }
}

export enum VehicleType {
  Car = 'Car',
  Motorcycle = 'Motorcycle',
  Van = 'Van',
}
