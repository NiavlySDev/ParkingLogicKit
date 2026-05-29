import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../../Rest/RestServer';
import { Router } from '@angular/router';
import { PrimengModule } from '../../../shared/primeng.module';
import { Vehicle } from '../../../../Auth/Vehicle';
import { AuthService } from '../../../../Auth/auth.service'; // Import requis pour la sécurité

@Component({
  selector: 'app-modify-vehicle',
  standalone: true,
  imports: [FormsModule, CommonModule, PrimengModule],
  templateUrl: './modify-vehicle.html',
  styleUrl: './modify-vehicle.css',
})
export class ModifyVehicle implements OnInit {
  brand: string = '';
  numberPlate: string = '';
  selectedVehicleType: number | null = null;

  isLoading: boolean = false;
  message: string = '';
  messageType: 'success' | 'error' = 'success';

  vehicles: any[] = [];
  selectedVehicle: any;

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
    const currentUsername = await this.authService.getUsername();

    if (!currentUsername || currentRole !== 'Admin') {
      console.warn('Tentative d’accès non autorisé à l’écran de modification des véhicules.');
      this.authService.logout();
      this.router.navigate(['/sign-in']);
      return;
    }

    this.loadVehicles();
  }

  loadVehicles(): void {
    this.restServer
      .getVehicleService()
      .getAll()
      .subscribe({
        next: (data: any[]) => {
          this.ngZone.run(() => {
            this.vehicles = (data || []).map((v) => ({
              ...v,
              fullName: `${v.brand} | ${v.numberPlate} | ${v.type}`,
            }));
            this.cdr.detectChanges();
          });
        },
        error: (err) => {
          console.error('Erreur lors du chargement des véhicules :', err);
        },
      });
  }

  changeVehicle(): void {
    this.ngZone.run(() => {
      if (!this.selectedVehicle) {
        this.resetForm();
        return;
      }

      this.brand = this.selectedVehicle.brand;
      this.numberPlate = this.selectedVehicle.numberPlate;

      const typeList = ['Moto', 'Voiture', 'Camionnette', 'Camion'];
      const index = typeList.indexOf(this.selectedVehicle.type);
      this.selectedVehicleType = index !== -1 ? index : 1;

      this.cdr.detectChanges();
    });
  }

  onSubmit(): void {
    if (
      !this.selectedVehicle ||
      !this.brand ||
      !this.numberPlate ||
      this.selectedVehicleType === null
    ) {
      this.setMessage('Tous les champs sont obligatoires', 'error');
      return;
    }

    // BARRIÈRE DE SÉCURITÉ 2 : Validation stricte des formats (Anti-XSS / Anti-Injection)
    const brandRegex = /^[a-zA-Z0-9\s-]+$/;
    const plateRegex = /^[A-Z]{2}-[0-9]{3}-[A-Z]{2}$/i;

    const sanitizedBrand = this.brand.trim().replace(/[<>"/\\;`]/g, '');
    const sanitizedPlate = this.numberPlate.trim().toUpperCase();

    if (!brandRegex.test(sanitizedBrand)) {
      this.setMessage(
        'Format de la marque invalide (lettres, chiffres, espaces et tirets uniquement).',
        'error'
      );
      return;
    }

    if (!plateRegex.test(sanitizedPlate)) {
      this.setMessage('Format de plaque d’immatriculation invalide (Ex: AA-123-BB).', 'error');
      return;
    }

    this.isLoading = true;
    this.message = '';

    const javaClassName = 'lml.snir.parkinglogickit.metier.entity.Vehicle';

    // BARRIÈRE DE SÉCURITÉ 3 : Forçage des types primitifs et préservation de l'owner d'origine
    const vehicleToUpdate: any = {
      id: Number(this.selectedVehicle.id),
      brand: sanitizedBrand,
      numberPlate: sanitizedPlate,
      type: this.getVehicleTypeName(this.selectedVehicleType),
      owner: this.selectedVehicle.owner || 'Inconnu', // On conserve le propriétaire d'origine de l'entité
      class: javaClassName,
    };

    this.restServer
      .getVehicleService()
      .update(vehicleToUpdate as Vehicle)
      .subscribe({
        next: () => {
          this.ngZone.run(() => {
            this.isLoading = false;
            this.setMessage('Modification effectuée avec succès ✅', 'success');
            this.resetForm();
            this.loadVehicles();
          });
        },
        error: (error: any) => {
          this.ngZone.run(() => {
            this.isLoading = false;
            console.error('Erreur lors de la mise à jour (update) du véhicule :', error);
            this.setMessage('Erreur serveur lors de la mise à jour', 'error');
            this.cdr.detectChanges();
          });
        },
      });
  }

  private getVehicleTypeName(index: number): string {
    const types = ['Moto', 'Voiture', 'Camionnette', 'Camion'];
    return types[index] || 'Voiture';
  }

  private setMessage(message: string, type: 'success' | 'error'): void {
    this.message = message;
    this.messageType = type;
  }

  private resetForm(): void {
    this.brand = '';
    this.numberPlate = '';
    this.selectedVehicleType = null;
    this.selectedVehicle = null;
    this.cdr.detectChanges();
  }

  goHome(): void {
    this.router.navigate(['/reception-admin']);
  }
}
