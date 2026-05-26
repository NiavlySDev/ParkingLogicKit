import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RestServer } from '../../../Rest/RestServer';
import { AuthService } from '../../../Auth/auth.service';
import { Vehicle } from '../../../Auth/Vehicle';
import { Associate } from '../../../Rest/AssociateService';
import { PrimengModule } from '../../shared/primeng.module';
import { switchMap } from 'rxjs';

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

  // FIX COMPILATION : Gestion de l'état des onglets de la Navbar
  private _activeTab: string = 'profile';

  // Formulaire réactif sécurisé
  vehicleForm!: FormGroup;

  // Tous les véhicules du système
  allVehicles: any[] = [];
  // Associations du driver connecté
  associations: Associate[] = [];
  // Véhicules associés au driver (affichés)
  vehicles: any[] = [];

  readonly vehicleTypeNames = ['Moto', 'Voiture', 'Camionnette', 'Camion'];
  isLoading: boolean = false;
  message: string = '';
  messageType: string = 'success';

  constructor(
    private authService: AuthService,
    private restServer: RestServer,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    private router: Router,
    private fb: FormBuilder
  ) {}

  async ngOnInit(): Promise<void> {
    this.username = (await this.authService.getUsername()) || 'Utilisateur';
    this.role = (await this.authService.getRole()) || 'Driver';

    // Initialisation des règles de validation du formulaire
    this.initForm();

    // Cette méthode s'exécutera une fois que this.username aura sa vraie valeur
    this.loadDriverThenVehicles();
  }

  // Getter et Setter pour activeTab : intercepte le clic "accueil" du HTML pour rediriger
  get activeTab(): string {
    return this._activeTab;
  }

  set activeTab(value: string) {
    this._activeTab = value;
    if (value === 'accueil') {
      this.goHome(); // Déclenche automatiquement la redirection vers l'écran principal du parking
    }
  }

  private initForm(): void {
    this.vehicleForm = this.fb.group({
      brand: [
        '',
        [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(30),
          // Anti-XSS / Injection : Autorise uniquement lettres, chiffres, espaces et tirets
          Validators.pattern(/^[a-zA-Z0-9\s-]+$/),
        ],
      ],
      numberPlate: [
        '',
        [
          Validators.required,
          // Validation stricte du format d'immatriculation (Ex: AA-123-BB)
          Validators.pattern(/^[A-Z]{2}-[0-9]{3}-[A-Z]{2}$/i),
        ],
      ],
      selectedType: [null, Validators.required],
    });
  }

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
            this.driverId = driver.id;
            this.loadVehiclesAndAssociations();
          } else {
            this.message = 'Erreur : Profil conducteur introuvable.';
            this.messageType = 'danger';
          }
        },
        error: (err) => {
          console.error('Erreur récupération drivers :', err);
          this.message = 'Impossible de charger les données du profil.';
          this.messageType = 'danger';
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
                    (a: any) => a.driver?.id === this.driverId || a.driverId === this.driverId
                  );

                  this.vehicles = this.associations
                    .map((a: any) => {
                      const vehicleId = a.vehicle?.id ?? a.vehicleId;
                      return this.allVehicles.find((v) => v.id === vehicleId);
                    })
                    .filter((v) => v !== undefined);

                  this.cdr.detectChanges();
                });
              },
              error: (err) => console.error('Erreur récupération associations :', err),
            });
        },
        error: (err) => console.error('Erreur récupération véhicules :', err),
      });
  }

  deleteVehicle(vehicle: any): void {
    const assoc = this.associations.find((a: any) => (a.vehicle?.id ?? a.vehicleId) === vehicle.id);

    if (!assoc) {
      this.message = "Erreur : Lien d'association introuvable.";
      this.messageType = 'danger';
      return;
    }

    this.restServer
      .getAssociateService()
      .remove(assoc)
      .pipe(switchMap(() => this.restServer.getVehicleService().remove(vehicle)))
      .subscribe({
        next: () => {
          this.ngZone.run(() => {
            this.loadVehiclesAndAssociations();
            this.message = 'Véhicule supprimé avec succès.';
            this.messageType = 'success';
            this.cdr.detectChanges();
          });
        },
        error: (err) => {
          console.error('Erreur suppression véhicule :', err);
          this.message = 'Erreur lors de la suppression du véhicule.';
          this.messageType = 'danger';
        },
      });
  }

  onSubmit(): void {
    if (this.vehicleForm.invalid) {
      this.message = 'Le formulaire contient des données invalides.';
      this.messageType = 'danger';
      return;
    }

    this.isLoading = true;
    this.message = '';

    const formValues = this.vehicleForm.value;
    const typeIndex = Number(formValues.selectedType);

    const vehicleData: any = {
      brand: formValues.brand.trim(),
      numberPlate: formValues.numberPlate.trim().toUpperCase(),
      type: this.vehicleTypeNames[typeIndex],
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
            console.error('ID du véhicule manquant à la création');
            this.isLoading = false;
            return;
          }

          const association: any = {
            driver: { id: Number(this.driverId) },
            vehicle: { id: Number(vehicleId) },
            badge: { id: 1 },
            class: 'lml.snir.parkinglogickit.metier.entity.Associate',
          };

          this.restServer
            .getAssociateService()
            .add(association)
            .subscribe({
              next: () => {
                this.ngZone.run(() => {
                  this.isLoading = false;
                  this.message = 'Véhicule enregistré et associé avec succès !';
                  this.messageType = 'success';
                  this.vehicleForm.reset({ selectedType: null });
                  this.loadVehiclesAndAssociations();
                });
              },
              error: (err) => {
                this.isLoading = false;
                console.error('Erreur création association :', err);
                this.message = "Le véhicule a été créé, mais l'association a échoué.";
                this.messageType = 'danger';
              },
            });
        },
        error: (err) => {
          this.isLoading = false;
          console.error('Erreur ajout véhicule :', err);
          this.message = "Erreur technique : Impossible d'ajouter le véhicule.";
          this.messageType = 'danger';
        },
      });
  }
}