import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../../Rest/RestServer';
import { Router } from '@angular/router';
import { PrimengModule } from '../../../shared/primeng.module';
import { Driver } from '../../../../Auth/Driver';
import { AuthService } from '../../../../Auth/auth.service'; // Ajout requis pour la sécurité
import { switchMap, from, concatMap, of } from 'rxjs';

@Component({
  selector: 'app-delete-user',
  standalone: true,
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
  currentAdminUsername: string = ''; // Stockage de session pour éviter l'auto-suppression

  constructor(
    private restServer: RestServer,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    private authService: AuthService // Injection du service d'authentification
  ) {}

  async ngOnInit(): Promise<void> {
    // BARRIÈRE DE SÉCURITÉ 1 : Contrôle d'accès strict au rôle Admin
    const currentRole = await this.authService.getRole();
    this.currentAdminUsername = (await this.authService.getUsername()) || '';
    
    if (!this.currentAdminUsername || currentRole !== 'Admin') {
      console.warn('Tentative d’accès non autorisé à la suppression d’utilisateurs.');
      this.authService.logout();
      this.router.navigate(['/sign-in']);
      return;
    }

    this.loadDrivers();
  }

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
        error: (err) => console.error('Erreur de chargement des profils :', err),
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

    // BARRIÈRE DE SÉCURITÉ 2 : Protection contre l'auto-suppression accidentelle ou malveillante
    const targetUsername = this.selectedDriver.login || this.selectedDriver.username || '';
    if (targetUsername === this.currentAdminUsername && this.currentAdminUsername !== '') {
      this.setMessage('Action interdite : Vous ne pouvez pas supprimer votre propre compte Administrateur connecté.', 'error');
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

    // Sécurisation et forçage des types pour l'intégrité de l'entité JPA cible
    const DriverData: any = {
      id: Number(this.selectedDriver.id),
      firstName: String(this.firstname).trim().replace(/[<>"/\\;`]/g, ''),
      lastName: String(this.lastName).trim().replace(/[<>"/\\;`]/g, ''),
      class: DriverClass,
    };

    // Flux RxJS optimisé pour éviter les ruptures de requêtes synchrones sur BDD distante
    this.restServer
      .getAssociateService()
      .getAll()
      .pipe(
        switchMap((associates) => {
          const linked = (associates || []).filter((a: any) => Number(a.driver?.id ?? a.driverId) === Number(DriverData.id));

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
                    const vehicleId = assoc.vehicle?.id ?? assoc.vehicleId;
                    if (!vehicleId) return of(null);
                    return this.restServer
                      .getVehicleService()
                      .remove({ id: Number(vehicleId), class: assoc.vehicle?.class || 'lml.snir.parkinglogickit.metier.entity.Vehicle' } as any);
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
            this.setMessage('Utilisateur et dépendances supprimés avec succès 🎉', 'success');
            this.resetForm();
            this.loadDrivers();
          });
        },
        error: (error: any) => {
          this.ngZone.run(() => {
            this.isLoading = false;
            console.error('Échec transactionnel de suppression :', error);
            this.setMessage("Erreur lors de la suppression. Vérifiez les dépendances en base.", 'error');
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
    this.selectedDriver = null;
    this.cdr.detectChanges();
  }
}