import { Component, ChangeDetectorRef, NgZone, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AssociateService } from '../../../../Rest/AssociateService';
import { AuthService } from '../../../../Auth/auth.service';
import { Capacitor } from '@capacitor/core';
import { REST_API_URL } from '../../../../Rest/api.config';

@Component({
  selector: 'app-add-vehicle',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './add-vehicle.html',
  styleUrl: './add-vehicle.css',
})
export class AddVehicle implements OnInit {
  brand: string = '';
  numberPlate: string = '';
  selectedVehicleType: number | null = null;
  badgeContent: string = '';

  isLoading: boolean = false;
  message: string = '';
  messageType: 'success' | 'error' = 'success';
  driver: any = null;

  constructor(
    private router: Router,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    private associateService: AssociateService,
    private authService: AuthService
  ) {}

  async ngOnInit(): Promise<void> {
    // BARRIÈRE DE SÉCURITÉ 1 : Contrôle d'accès strict au rôle Admin
    const currentRole = await this.authService.getRole();
    const currentUsername = await this.authService.getUsername();

    if (!currentUsername || currentRole !== 'Admin') {
      console.warn('Tentative d’accès non autorisé à l’écran Admin.');
      this.authService.logout();
      this.router.navigate(['/sign-in']);
      return;
    }

    // Chargement sécurisé de la donnée du conducteur transmis
    if (Capacitor.isNativePlatform()) {
      const { SecureStoragePlugin } = await import('capacitor-secure-storage-plugin');
      try {
        const { value } = await SecureStoragePlugin.get({ key: 'selected_driver' });
        this.driver = value ? JSON.parse(value) : null;
      } catch {
        this.driver = null;
      }
    } else {
      const localData = localStorage.getItem('driver');
      this.driver = localData ? JSON.parse(localData) : null;
    }

    // Si l'état initial est corrompu, on évite de laisser l'Admin sur une page vide
    if (!this.driver) {
      this.setMessage('Erreur : Aucun conducteur sélectionné pour cette opération.', 'error');
    }
  }

  goHome(): void {
    this.router.navigate(['/reception-admin']);
  }

  async onSubmit(): Promise<void> {
    // BARRIÈRE DE SÉCURITÉ 2 : Validation stricte des données (Anti-Injection / XSS)
    if (
      !this.brand ||
      !this.numberPlate ||
      this.selectedVehicleType === null ||
      !this.badgeContent.trim()
    ) {
      this.setMessage('Tous les champs sont obligatoires.', 'error');
      return;
    }

    if (!this.driver) {
      this.setMessage('Conducteur introuvable. Opération annulée.', 'error');
      return;
    }

    // Nettoyage et Expressions régulières de contrôle (White-listing)
    const brandRegex = /^[a-zA-Z0-9\s-]+$/;
    const plateRegex = /^[A-Z]{2}-[0-9]{3}-[A-Z]{2}$/i;
    const badgeRegex = /^[a-zA-Z0-9-]+$/; // Modifie selon la syntaxe physique de tes badges (ex: HEX ou UID)

    const sanitizedBrand = this.brand.trim().replace(/[<>"/\\;`]/g, '');
    const sanitizedPlate = this.numberPlate.trim().toUpperCase();
    const sanitizedBadge = this.badgeContent.trim();

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

    if (!badgeRegex.test(sanitizedBadge)) {
      this.setMessage(
        'Format du contenu du badge invalide (Caractères alphanumériques uniquement).',
        'error'
      );
      return;
    }

    this.isLoading = true;
    this.message = '';

    const vehicleTypeNames = ['Moto', 'Voiture', 'Camionnette', 'Camion'];

    const VehicleData: any = {
      brand: sanitizedBrand,
      numberPlate: sanitizedPlate,
      type: vehicleTypeNames[this.selectedVehicleType] || 'Voiture',
      owner: this.driver?.login || this.driver?.username || 'Inconnu',
      class: 'lml.snir.parkinglogickit.metier.entity.Vehicle',
    };

    try {
      const token = await this.authService.getToken();

      // ========================================================
      // ÉTAPE 1 : CRÉATION DU VÉHICULE
      // ========================================================
      const res = await fetch(`${REST_API_URL}/VehicleService/`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(VehicleData),
      });

      if (!res.ok) {
        throw new Error(`Erreur HTTP création véhicule: ${res.status}`);
      }

      const createdVehicle = await res.json();

      if (!createdVehicle || !createdVehicle.id) {
        this.setMessage('Erreur : Réponse serveur véhicule non valide.', 'error');
        this.isLoading = false;
        this.cdr.detectChanges();
        return;
      }

      // ========================================================
      // ÉTAPE 2 : CRÉATION DU BADGE
      // ========================================================
      const BadgeData: any = {
        content: sanitizedBadge,
        class: 'lml.snir.parkinglogickit.metier.entity.Badge',
      };

      const badgeRes = await fetch(`${REST_API_URL}/BadgeService/`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(BadgeData),
      });

      if (!badgeRes.ok) {
        throw new Error(`Erreur HTTP création badge: ${badgeRes.status}`);
      }

      const createdBadge = await badgeRes.json();

      if (!createdBadge || !createdBadge.id) {
        this.setMessage('Erreur : Réponse serveur badge non valide.', 'error');
        this.isLoading = false;
        this.cdr.detectChanges();
        return;
      }

      // ========================================================
      // ÉTAPE 3 : CRÉATION DE L'ASSOCIATION
      // ========================================================
      this.ngZone.run(async () => {
        const rawDriverId = this.driver?.id || this.driver?.DRIVER_ID;

        if (!rawDriverId) {
          this.setMessage("Erreur : Impossible d'identifier le conducteur cible.", 'error');
          this.isLoading = false;
          this.cdr.detectChanges();
          return;
        }

        this.associateService
          .add({
            driver: { id: Number(rawDriverId) },
            vehicle: { id: Number(createdVehicle.id) },
            badgeId: Number(createdBadge.id), // Cohérence typage plat
            badge: { id: Number(createdBadge.id) }, // Cohérence relationnelle imbriquée
            class: 'lml.snir.parkinglogickit.metier.entity.Associate',
          } as any)
          .subscribe({
            next: async () => {
              this.setMessage('Conducteur, véhicule et badge associés avec succès ! 🎉', 'success');

              // Nettoyage sécurisé des résidus de stockage
              if (Capacitor.isNativePlatform()) {
                const { SecureStoragePlugin } = await import('capacitor-secure-storage-plugin');
                await SecureStoragePlugin.remove({ key: 'selected_driver' });
              } else {
                localStorage.removeItem('driver');
              }

              this.resetForm();
              this.isLoading = false;
              this.cdr.detectChanges();

              setTimeout(() => {
                this.goHome();
              }, 1500);
            },
            error: (err) => {
              console.error('Erreur transactionnelle association :', err);
              this.setMessage(
                "L'enregistrement du véhicule a réussi mais l'association a été rejetée.",
                'error'
              );
              this.isLoading = false;
              this.cdr.detectChanges();
            },
          });
      });
    } catch (err) {
      console.error('Erreur processus critique :', err);
      this.isLoading = false;
      this.setMessage("Une erreur technique s'est produite lors de l'enregistrement.", 'error');
      this.cdr.detectChanges();
    }
  }

  private setMessage(message: string, type: 'success' | 'error'): void {
    this.message = message;
    this.messageType = type;
  }

  private resetForm(): void {
    this.brand = '';
    this.numberPlate = '';
    this.selectedVehicleType = null;
    this.badgeContent = '';
  }
}
