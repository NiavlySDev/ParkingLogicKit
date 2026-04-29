import { Component, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../../Rest/RestServer';
import { Driver } from '../../../../Auth/Driver.js';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './sign-up.html',
  styleUrls: ['./sign-up.css'], // Correction ici
})
export class SignUp {
  firstname: string = '';
  lastName: string = '';
  username: string = '';
  password: string = '';
  age: number | null = null;
  isMale: boolean | null = null;
  DriverType: number | null = null;
  addCar: string | null = null; // Ajout de la propriété pour le champ "addCar"
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
    // Validation des champs
    if (
      !this.firstname ||
      !this.lastName ||
      !this.username ||
      !this.password ||
      this.age === null ||
      this.isMale === null ||
      this.DriverType === null ||
      this.addCar === null // Validation pour le champ "addCar"
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
        : 'lml.snir.parkinglogickit.metier.entity.Driver';

    const DriverData: any = {
      firstName: this.firstname,
      lastName: this.lastName,
      username: this.username,
      password: this.password,
      age: this.age,
      isMale: this.isMale,
      class: DriverClass,
    };

    this.restServer
      .getDriverService()
      .add(DriverData as Driver)
      .subscribe({
        next: (createdDriver: any) => {
          this.ngZone.run(() => {
            this.isLoading = false;
            localStorage.setItem('driver', JSON.stringify(createdDriver));
            this.setMessage('Inscription réussie 🎉', 'success');
            this.router.navigate(['/add-vehicle']);

            this.resetForm();
            this.cdr.detectChanges();
          });
        },
        error: (error: any) => {
          this.ngZone.run(() => {
            this.isLoading = false;
            console.error('Erreur lors de l’inscription :', error); // Journalisation de l'erreur
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
    this.firstname = '';
    this.lastName = '';
    this.username = '';
    this.password = '';
    this.age = null;
    this.isMale = null;
    this.DriverType = null;
    this.addCar = null; // Réinitialisation du champ "addCar"
  }
}