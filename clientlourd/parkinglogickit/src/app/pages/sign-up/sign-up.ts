import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../Rest/RestServer';
import { Driver } from '../../../Auth/Driver.js';

// author Ethan

@Component({
  selector: 'app-sign-up',
  imports: [FormsModule, CommonModule],
  templateUrl: './sign-up.html',
  styleUrl: './sign-up.css',
})
export class signUp {
  firstname: string = '';
  surname: string = '';
  login: string = '';
  password: string = '';
  age: number | null = null;

  // devient un booléen pour la structure "isMasculin"
  isMasculin: boolean | null = null;

  // sert seulement à déterminer la classe envoyée
  DriverType: number | null = null;

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
      //this.age === null ||
      this.isMasculin === null ||
      this.DriverType === null
    ) {
      this.setMessage('Tous les champs sont obligatoires', 'error');
      return;
    }

    if (this.age === null || this.age! < 1 || this.age! > 120) {
      this.setMessage("L'âge doit être entre 1 et 120 ans", 'error');
      return;
    }

    this.isLoading = true;
    this.message = '';

    // Détermination de la classe Java
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
      //age: this.age,
      isMasculin: this.isMasculin,
      class: DriverClass,
    };

    console.log('JSON envoyé:', JSON.stringify(DriverData, null, 2));

    this.restServer
      .getDriverService()
      .add(DriverData as Driver)
      .subscribe({
        next: (response: Driver) => {
          this.isLoading = false;
          this.setMessage('Sign-up réussie!', 'success');
          this.resetForm();
        },
        error: (error: any) => {
          this.isLoading = false;
          const msg =
            error.error?.message || error.message || "Une erreur s'est produite lors de l'Sign-up";
          this.setMessage(msg, 'error');
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
    //this.age = null;
    this.isMasculin = null;
    this.DriverType = null;
  }
}
