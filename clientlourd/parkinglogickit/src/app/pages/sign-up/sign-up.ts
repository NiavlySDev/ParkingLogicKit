import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../Rest/RestServer';
import { Driver } from '../../../Auth/Driver.js';

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
  surname: string = '';
  login: string = '';
  password: string = '';
  age: number | null = null;

  isMasculin: boolean | null = null;
  DriverType: number | null = null;
  addCar: VehicleType | null = null;
  VehicleType = VehicleType; // exposé au HTML

  isLoading: boolean = false;
  message: string = '';
  messageType: 'success' | 'error' = 'success';

  constructor(private restServer: RestServer) {}

  onSubmit(): void {
    if (
      !this.firstname ||
      !this.surname ||
      !this.login ||
      !this.password ||
      this.age === null ||
      this.isMasculin === null ||
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
        ? 'lml.snir.ParkingLogicKit.metier.entity.Administrator'
        : 'lml.snir.ParkingLogicKit.metier.entity.Driver';

    const DriverData: any = {
      id: 0,
      name: this.surname,
      firstName: this.firstname,
      login: this.login,
      password: this.password,
      age: this.age,
      isMasculin: this.isMasculin,
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
    this.surname = '';
    this.login = '';
    this.password = '';
    this.age = null;
    this.isMasculin = null;
    this.DriverType = null;
    this.addCar = null;
  }
}

export enum VehicleType {
  Car = 'Car',
  Motorcycle = 'Motorcycle',
  Van = 'Van',
}
