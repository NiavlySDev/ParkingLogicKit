import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../../Rest/RestServer';
import { Driver } from '../../../../Auth/Driver.js';
import { Router } from '@angular/router';
import { PrimengModule } from '../../../shared/primeng.module';
import { PrimeIcons } from 'primeng/api';

@Component({
  selector: 'app-modify-user',
  imports: [FormsModule, CommonModule, PrimengModule],
  templateUrl: './modify-user.html',
  styleUrl: './modify-user.css',
})
export class ModifyUser implements OnInit {
  firstname: string = '';
  lastName: string = '';
  username: string = '';
  password: string = '';
  age: number | null = null;
  isMale: boolean | null = null;
  DriverType: number | null = null;

  isLoading: boolean = false;
  message: string = '';
  messageType: 'success' | 'error' = 'success';

  drivers: any[] = [];
  selectedDriver: any;
  showPassword: boolean = false;

  constructor(
    private restServer: RestServer,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone
  ) {}

  ngOnInit() {
    this.restServer
      .getDriverService()
      .getAll()
      .subscribe({
        next: (drivers: any[]) => {
          this.ngZone.run(() => {
            this.drivers = drivers.map((d) => ({ ...d, fullName: `${d.firstName} ${d.lastName}` }));
          });
        },
      });
  }

  goHome(): void {
    this.router.navigate(['/reception-admin']);
  }

  onSubmit(): void {
    if (
      !this.selectedDriver ||
      !this.firstname ||
      !this.lastName ||
      !this.username ||
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
        : this.DriverType === 1
        ? 'lml.snir.parkinglogickit.metier.entity.Maintenance'
        : 'lml.snir.parkinglogickit.metier.entity.Driver';

    const DriverData: any = {
      id: this.selectedDriver.id,
      firstName: this.firstname,
      lastName: this.lastName,
      username: this.username,
      ...(this.password ? { password: this.password } : {}),
      age: this.age,
      isMale: this.isMale,
      class: DriverClass,
    };

    this.restServer
      .getDriverService()
      .update(DriverData as Driver)
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
    this.firstname = '';
    this.lastName = '';
    this.username = '';
    this.password = '';
    this.age = null;
    this.isMale = null;
    this.DriverType = null;
    this.selectedDriver = null;
  }

  changeDriver(): void {
    this.ngZone.run(() => {
      if (!this.selectedDriver) {
        this.setMessage('Veuillez sélectionner un Driver', 'error');
        return;
      }

      this.firstname = this.selectedDriver.firstName;
      this.lastName = this.selectedDriver.lastName;
      this.username = this.selectedDriver.username;
      this.password = '';
      this.age = this.selectedDriver.age;
      this.isMale =
        this.selectedDriver.isMale === true ||
        this.selectedDriver.isMale === 1 ||
        this.selectedDriver.masculin === 1;
      this.DriverType =
        this.selectedDriver.class === 'lml.snir.parkinglogickit.metier.entity.Admin'
          ? 0
          : this.selectedDriver.class === 'lml.snir.parkinglogickit.metier.entity.Maintenance'
          ? 1
          : 2;

      this.cdr.detectChanges();
    });
  }

  generateMdp(): void {
    if (!this.selectedDriver) {
      this.setMessage('Veuillez sélectionner un Driver', 'error');
      return;
    }
    this.password = this.generateRandomPassword();
  }

  private generateRandomPassword(length: number = 12): string {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+';
    let password = '';
    for (let i = 0; i < length; i++) {
      password += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return password;
  }

  copyMdp(): void {
    if (!this.password) {
      this.setMessage('Veuillez générer un mot de passe avant de le copier', 'error');
      return;
    }

    navigator.clipboard
      .writeText(this.password)
      .then(() => {
        this.setMessage('Le mot de passe a été copié dans le presse-papier', 'success');
        this.cdr.detectChanges();
      })
      .catch(() => {
        this.setMessage('Échec de la copie du mot de passe', 'error');
        this.cdr.detectChanges();
      });
  }
}
