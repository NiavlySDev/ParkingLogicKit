import { Component, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../../Rest/RestServer';
import { Driver } from '../../../../Auth/Driver';
import { Router } from '@angular/router';
import { Capacitor } from '@capacitor/core'; // Pour la détection de la plateforme

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './sign-up.html',
  styleUrls: ['./sign-up.css'],
})
export class SignUp {
  firstname: string = '';
  lastName: string = '';
  username: string = '';
  password: string = '';
  age: number | null = null;
  isMale: boolean | null = null;
  DriverType: number | null = null;
  addCar: string | null = null;
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
    if (
      !this.firstname ||
      !this.lastName ||
      !this.username ||
      !this.password ||
      this.age === null ||
      this.isMale === null ||
      this.DriverType === null
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
      firstName: this.firstname.trim(),
      lastName: this.lastName.trim(),
      username: this.username.trim(),
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
          this.ngZone.run(async () => {
            this.isLoading = false;
            this.setMessage('Inscription réussie 🎉', 'success');

            if (Capacitor.isNativePlatform()) {
              const { SecureStoragePlugin } = await import('capacitor-secure-storage-plugin');
              await SecureStoragePlugin.set({
                key: 'selected_driver',
                value: JSON.stringify(createdDriver),
              });
            } else {
              localStorage.setItem('driver', JSON.stringify(createdDriver));
            }

            this.router.navigate(['/add-vehicle']);
            this.resetForm();
          });
        },
        error: (error: any) => {
          this.ngZone.run(() => {
            this.isLoading = false;
            console.error('Erreur lors de l’inscription :', error);
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
    this.addCar = null;
    this.cdr.detectChanges();
  }
}
