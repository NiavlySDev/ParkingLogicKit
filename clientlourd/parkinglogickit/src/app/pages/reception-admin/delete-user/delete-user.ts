import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../../Rest/RestServer';
import { Router } from '@angular/router';
import { PrimengModule } from '../../../shared/primeng.module';
import { Driver } from '../../../../Auth/Driver';

@Component({
  selector: 'app-delete-user',
  imports: [FormsModule, CommonModule, PrimengModule],
  templateUrl: './delete-user.html',
  styleUrl: './delete-user.css',
})
export class DeleteUser implements OnInit {

  firstname: string = '';
  lastName: string = '';
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
    private ngZone: NgZone,
  ) {}

  ngOnInit() {
    this.restServer.getDriverService().getAll().subscribe({
      next: (drivers: any[]) => {
        this.ngZone.run(() => {
          this.drivers = drivers.map(d => ({ ...d, fullName: `${d.firstName} ${d.lastName}` }));
        });
      }
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
      this.DriverType === null
    ) {
      this.setMessage('Tous les champs sont obligatoires', 'error');
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
      class: DriverClass,
    };
        this.restServer
          .getDriverService()
          .remove(DriverData as Driver)
          .subscribe({
            next: () => {
              this.ngZone.run(() => {           // 👈 ajout
                this.isLoading = false;
                this.setMessage('Driver supprimé avec succès 🎉', 'success');
                this.resetForm();
                this.cdr.detectChanges();       // 👈 ajout
              });
            },
            error: (error: any) => {
              this.ngZone.run(() => {           // 👈 ajout
                this.isLoading = false;
                this.setMessage(error?.error?.message || "Une erreur s'est produite", 'error');
                this.cdr.detectChanges();       // 👈 ajout
              });
            },
          });
      

  }

  private setMessage(message: string, type: 'success' | 'error'): void {
    this.message = message;
    this.messageType = type;
  }

  changeDriver(): void {
    this.ngZone.run(() => {
      if (!this.selectedDriver) {
        this.setMessage('Veuillez sélectionner un Driver', 'error');
        return;
      }

      this.firstname = this.selectedDriver.firstName;
      this.lastName = this.selectedDriver.lastName;
      this.DriverType =
        this.selectedDriver.class === 'lml.snir.parkinglogickit.metier.entity.Admin'
          ? 0
          : this.selectedDriver.class === 'lml.snir.parkinglogickit.metier.entity.Maintenance'
            ? 1
            : 2;

      this.cdr.detectChanges();
    });
    
  }

  private resetForm(): void {
    this.firstname = '';
    this.lastName = '';
    this.DriverType = null;
  }

}