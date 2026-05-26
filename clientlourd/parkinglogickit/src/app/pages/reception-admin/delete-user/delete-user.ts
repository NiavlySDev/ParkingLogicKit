import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../../Rest/RestServer';
import { Router } from '@angular/router';
import { PrimengModule } from '../../../shared/primeng.module';
import { Driver } from '../../../../Auth/Driver';
import { switchMap, from, concatMap } from 'rxjs';

@Component({
  selector: 'app-delete-user',
  standalone: true, // Ajout explicite pour sécuriser le comportement standalone
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
    private ngZone: NgZone
  ) {}

  ngOnInit(): void {
    this.loadDrivers();
  }

  /**
   * Charge et rafraîchit la liste des conducteurs depuis le serveur Java
   */
  private loadDrivers(): void {
    this.restServer
      .getDriverService()
      .getAll()
      .subscribe({
        next: (drivers: any[]) => {
          this.ngZone.run(() => {
            this.drivers = (drivers || []).map((d) => ({
              ...d,
              fullName: `${d.firstName} ${d.lastName}`,
            }));
            this.cdr.detectChanges();
          });
        },
        error: (err) => console.error('Erreur chargement drivers :', err),
      });
  }

  goHome(): void {
    this.router.navigate(['/reception-admin']);
  }

  onSubmit(): void {
    if (!this.selectedDriver || !this.firstname || !this.lastName || this.DriverType === null) {
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
      .getAssociateService()
      .getAll()
      .pipe(
        switchMap((associates) => {
          const linked = associates.filter((a: any) => a.driver?.id === this.selectedDriver.id);

          if (linked.length === 0) {
            return this.restServer.getDriverService().remove(DriverData as Driver);
          }

          return from(linked).pipe(
            concatMap((assoc: any) => {
              return this.restServer
                .getAssociateService()
                .remove(assoc)
                .pipe(
                  switchMap(() => {
                    return this.restServer
                      .getVehicleService()
                      .remove({ id: assoc.vehicle?.id, class: assoc.vehicle?.class } as any);
                  })
                );
            }),
            switchMap(() => this.restServer.getDriverService().remove(DriverData as Driver))
          );
        })
      )
      .subscribe({
        next: () => {
          this.ngZone.run(() => {
            this.isLoading = false;
            this.setMessage('Driver supprimé avec succès 🎉', 'success');
            this.resetForm();
            this.loadDrivers();
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
    this.selectedDriver = null; // Nettoie la sélection de l'ancien utilisateur supprimé
    this.cdr.detectChanges();
  }
}
