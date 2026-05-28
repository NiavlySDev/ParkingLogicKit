import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RestServer } from '../../../Rest/RestServer';
import { AuthService } from '../../../Auth/auth.service';
import { Vehicle } from '../../../Auth/Vehicle';
import { Associate } from '../../../Rest/AssociateService';
import { PrimengModule } from '../../shared/primeng.module';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, PrimengModule],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.css',
})
export class UserProfile implements OnInit {
  menuOpen: boolean = false;
  username: string = '';
  role: string = '';
  driverId: number = 0;

  private _activeTab: string = 'profile';

  vehicleForm = new FormGroup({
    brand: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(30),
        Validators.pattern(/^[a-zA-Z0-9\s-]+$/),
      ],
    }),
    numberPlate: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^[A-Z]{2}-[0-9]{3}-[A-Z]{2}$/)],
    }),
    selectedType: new FormControl<number | null>(null, [Validators.required]),
  });

  allVehicles: any[] = [];
  associations: Associate[] = [];
  vehicles: any[] = [];
  driverBadgeId: number | null = null;

  readonly vehicleTypeNames = ['Moto', 'Voiture', 'Camionnette', 'Camion'];
  isLoading: boolean = false;
  message: string = '';
  messageType: string = 'success';

  constructor(
    private authService: AuthService,
    private restServer: RestServer,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    private router: Router
  ) {}

  async ngOnInit(): Promise<void> {
    this.username = (await this.authService.getUsername()) || '';
    this.role = (await this.authService.getRole()) || 'Driver';

    if (!this.username) {
      this.logout();
      return;
    }

    this.cdr.detectChanges();
    this.loadDriverThenVehicles();
  }

  get activeTab(): string {
    return this._activeTab;
  }

  set activeTab(value: string) {
    this._activeTab = value;
    if (value === 'accueil') {
      this.goHome();
    }
  }

  // Empêche les mauvais formats, force les majuscules et ajoute les tirets automatiquement
  onPlaqueInput(event: any): void {
    const inputElement = event.target;
    const rawValue = inputElement.value.toUpperCase();

    //On nettoie pour enlever les tirets existants et travailler sur les caractères bruts
    const cleanValue = rawValue.replace(/[^A-Z0-9]/g, '');

    let formatted = '';

    // On reconstruit selon la structure AA-123-BB
    for (let i = 0; i < cleanValue.length; i++) {
      const char = cleanValue[i];

      // Les 2 premières positions acceptent uniquement des lettres
      if (i < 2) {
        if (/[A-Z]/.test(char)) {
          formatted += char;
        }
      }
      // Les 3 positions suivantes acceptent uniquement des chiffres
      else if (i >= 2 && i < 5) {
        if (i === 2 && formatted.length === 2) {
          formatted += '-';
        }
        if (/[0-9]/.test(char)) {
          formatted += char;
        }
      }
      // Les 2 dernières positions acceptent uniquement des lettres
      else if (i >= 5 && i < 7) {
        if (i === 5 && (formatted.length === 6 || formatted.length === 5)) {
          if (!formatted.endsWith('-')) {
            formatted += '-';
          }
        }
        if (/[A-Z]/.test(char)) {
          formatted += char;
        }
      }
    }

    // Gestion du retour arrière pour ne pas bloquer l'utilisateur sur un tiret
    const currentValue = this.vehicleForm.get('numberPlate')?.value || '';
    if (rawValue.length < currentValue.length && formatted.endsWith('-')) {
      formatted = formatted.slice(0, -1);
    }

    // Mise à jour de la valeur dans le formulaire Angular
    this.vehicleForm.patchValue({ numberPlate: formatted }, { emitEvent: false });

    // Force la valeur formatée sur le champ physique HTML
    inputElement.value = formatted;
  }

  // Alterne l'affichage du menu déroulant
  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  goHome(): void {
    if (this.role === 'Admin') {
      this.router.navigate(['/reception-admin']);
    } else {
      this.router.navigate(['/reception']);
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/sign-in']);
  }

  loadDriverThenVehicles(): void {
    this.restServer
      .getDriverService()
      .getAll()
      .subscribe({
        next: (drivers: any[]) => {
          const driver = drivers.find(
            (d: any) =>
              d.login === this.username || d.username === this.username || d.name === this.username
          );

          if (driver) {
            this.driverId = Number(driver.id);

            if (driver.badgeId) {
              this.driverBadgeId = Number(driver.badgeId);
            } else if (driver.badge?.id) {
              this.driverBadgeId = Number(driver.badge.id);
            }

            this.loadVehiclesAndAssociations();
          } else {
            this.message = 'Erreur : Profil conducteur introuvable.';
            this.messageType = 'danger';
            this.cdr.detectChanges();
          }
        },
        error: (err) => {
          console.error('Erreur de récupération :', err);
          this.message = 'Impossible de charger les données du profil.';
          this.messageType = 'danger';
          this.cdr.detectChanges();
        },
      });
  }

  goProfile(): void {
    this.menuOpen = false;
    this.activeTab = 'profile';
    this.router.navigate(['/user-profile']);
  }

  loadVehiclesAndAssociations(): void {
    this.restServer
      .getVehicleService()
      .getAll()
      .subscribe({
        next: (allVehicles: any[]) => {
          this.allVehicles = allVehicles || [];

          this.restServer
            .getAssociateService()
            .getAll()
            .subscribe({
              next: (allAssociations: Associate[]) => {
                this.ngZone.run(() => {
                  this.associations = (allAssociations || []).filter(
                    (a: any) => Number(a.driver?.id ?? a.driverId) === this.driverId
                  );

                  if (this.associations.length > 0) {
                    const firstAssoc: any = this.associations[0];
                    const extractedId =
                      firstAssoc.badgeId ?? firstAssoc.badge?.id ?? firstAssoc.idBadge;
                    if (extractedId) {
                      this.driverBadgeId = Number(extractedId);
                    }
                  }

                  this.vehicles = this.associations
                    .map((a: any) => {
                      const vehicleId = a.vehicle?.id ?? a.vehicleId;
                      return this.allVehicles.find((v) => v.id === vehicleId);
                    })
                    .filter((v) => v !== undefined);

                  this.cdr.detectChanges();
                });
              },
              error: (err) => console.error('Erreur accès associations :', err),
            });
        },
        error: (err) => console.error('Erreur accès véhicules :', err),
      });
  }

  deleteVehicle(vehicle: any): void {
    const assoc = this.associations.find((a: any) => (a.vehicle?.id ?? a.vehicleId) === vehicle.id);

    if (!assoc) {
      this.message = 'Action interdite : Ce véhicule ne vous appartient pas.';
      this.messageType = 'danger';
      this.cdr.detectChanges();
      return;
    }

    this.restServer
      .getAssociateService()
      .remove(assoc)
      .subscribe({
        next: () => {
          this.restServer
            .getVehicleService()
            .remove(vehicle)
            .subscribe({
              next: () => {
                this.ngZone.run(() => {
                  this.loadVehiclesAndAssociations();
                  this.message = 'Véhicule supprimé avec succès.';
                  this.messageType = 'success';
                  this.cdr.detectChanges();
                });
              },
              error: () => {
                this.ngZone.run(() => {
                  this.loadVehiclesAndAssociations();
                  this.message = 'Véhicule supprimé avec succès.';
                  this.messageType = 'success';
                  this.cdr.detectChanges();
                });
              },
            });
        },
        error: (err) => {
          console.error('Échec de la suppression :', err);
          this.message = 'Erreur lors de la suppression.';
          this.messageType = 'danger';
          this.cdr.detectChanges();
        },
      });
  }

  async onSubmit(): Promise<void> {
    if (this.vehicleForm.invalid) {
      this.message = 'Le formulaire contient des données invalides.';
      this.messageType = 'danger';
      this.cdr.detectChanges();
      return;
    }

    const currentSessionUser = await this.authService.getUsername();

    if (!currentSessionUser || currentSessionUser !== this.username || !this.driverId) {
      this.message = 'Session compromise. Veuillez vous reconnecter.';
      this.messageType = 'danger';
      this.logout();
      return;
    }

    this.isLoading = true;
    this.message = '';
    this.cdr.detectChanges();

    const formValues = this.vehicleForm.value;
    const typeIndex = Number(formValues.selectedType);

    const sanitizedBrand = (formValues.brand || '').trim().replace(/[<>"/\\;`]/g, '');
    const sanitizedPlate = (formValues.numberPlate || '')
      .trim()
      .toUpperCase()
      .replace(/[^A-Z0-9-]/g, '');

    const vehicleData: any = {
      brand: sanitizedBrand,
      numberPlate: sanitizedPlate,
      type: this.vehicleTypeNames[typeIndex] || 'Voiture',
      owner: this.username,
      class: 'lml.snir.parkinglogickit.metier.entity.Vehicle',
    };

    this.restServer
      .getVehicleService()
      .add(vehicleData as Vehicle)
      .subscribe({
        next: (createdVehicle: any) => {
          const vehicleId = createdVehicle?.id;

          if (!vehicleId) {
            this.isLoading = false;
            this.message = 'Erreur de création.';
            this.messageType = 'danger';
            this.cdr.detectChanges();
            return;
          }

          const association: any = {
            driver: { id: Number(this.driverId) },
            vehicle: { id: Number(vehicleId) },
            badgeId: this.driverBadgeId ? Number(this.driverBadgeId) : null,
            badge: this.driverBadgeId ? { id: Number(this.driverBadgeId) } : null,
            class: 'lml.snir.parkinglogickit.metier.entity.Associate',
          };

          this.restServer
            .getAssociateService()
            .add(association)
            .subscribe({
              next: () => {
                this.ngZone.run(() => {
                  this.isLoading = false;
                  this.message = 'Véhicule enregistré et associé avec succès ! 🎉';
                  this.messageType = 'success';

                  this.vehicleForm.reset();
                  this.vehicleForm.get('selectedType')?.setValue(null);

                  this.loadVehiclesAndAssociations();
                });
              },
              error: (err) => {
                this.isLoading = false;
                console.error('Erreur de liaison :', err);
                this.message = "L'association a été rejetée par le serveur backend.";
                this.messageType = 'danger';
                this.cdr.detectChanges();
              },
            });
        },
        error: (err) => {
          this.isLoading = false;
          console.error('Erreur injection véhicule :', err);
          this.message = 'Action refusée par la passerelle applicative.';
          this.messageType = 'danger';
          this.cdr.detectChanges();
        },
      });
  }
}
