import { Component, ChangeDetectorRef, NgZone, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AssociateService } from '../../../../Rest/AssociateService';
import { AuthService } from '../../../../Auth/auth.service';
import { Capacitor } from '@capacitor/core';

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
  isLoading: boolean = false;
  message: string = '';
  messageType: 'success' | 'error' = 'success';

  // Contiendra les infos du driver à associer
  driver: any = null;

  constructor(
    private router: Router,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    private associateService: AssociateService,
    private authService: AuthService // Injection indispensable
  ) {}

  async ngOnInit(): Promise<void> {
    // CORRECTIF SÉCURITÉ : Récupération asynchrone compatible Android/Web
    // Note : Pense à utiliser authService pour sauvegarder le driver en amont si nécessaire
    if (Capacitor.isNativePlatform()) {
      // Si tu as migré le stockage du driver temporaire dans le Secure Storage
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
  }

  goHome(): void {
    this.router.navigate(['/reception-admin']);
  }

  async onSubmit(): Promise<void> {
    if (!this.brand || !this.numberPlate || this.selectedVehicleType === null) {
      this.setMessage('Tous les champs sont obligatoires', 'error');
      return;
    }

    if (!this.driver) {
      this.setMessage('Aucun driver trouvé', 'error');
      return;
    }

    this.isLoading = true;
    this.message = '';

    const vehicleTypeNames = ['Moto', 'Voiture', 'Camionnette', 'Camion'];

    const VehicleData: any = {
      brand: this.brand.trim(),
      numberPlate: this.numberPlate.trim().toUpperCase(), // Assainissement
      type: vehicleTypeNames[this.selectedVehicleType],
      class: 'lml.snir.parkinglogickit.metier.entity.Vehicle',
    };

    try {
      // CORRECTIF SÉCURITÉ : Utilisation de fetch en incluant le Token décrypté manuellement
      // ou en passant par l'intercepteur. Pour sécuriser fetch ici, on injecte le jeton :
      const token = await this.authService.getToken();

      const res = await fetch('/ParkingLogicKit/rest/VehicleService/', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`, // Protection contre le rejet du serveur Java
        },
        body: JSON.stringify(VehicleData),
      });

      if (!res.ok) {
        throw new Error(`Erreur HTTP création véhicule: ${res.status}`);
      }

      const createdVehicle = await res.json();

      if (!createdVehicle || !createdVehicle.id) {
        this.setMessage('Erreur: véhicule non valide', 'error');
        this.isLoading = false;
        this.cdr.detectChanges();
        return;
      }

      this.ngZone.run(async () => {
        this.associateService
          .add({
            driver: { id: Number(this.driver.id) },
            vehicle: { id: Number(createdVehicle.id) },
            badge: { id: 1 },
            class: 'lml.snir.parkinglogickit.metier.entity.Associate',
          } as any)
          .subscribe({
            next: async () => {
              this.setMessage('Driver associé au véhicule avec succès!', 'success');

              // Nettoyage hybride sécurisé
              if (Capacitor.isNativePlatform()) {
                const { SecureStoragePlugin } = await import('capacitor-secure-storage-plugin');
                await SecureStoragePlugin.remove({ key: 'selected_driver' });
              } else {
                localStorage.removeItem('driver');
              }

              this.resetForm();
              this.isLoading = false;
              this.cdr.detectChanges();
            },
            error: (err) => {
              console.error('Erreur association:', err);
              this.setMessage("Erreur lors de l'association", 'error');
              this.isLoading = false;
              this.cdr.detectChanges();
            },
          });
      });
    } catch (err) {
      console.error('Erreur création véhicule:', err);
      this.isLoading = false;
      this.setMessage("Une erreur s'est produite", 'error');
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
  }
}
