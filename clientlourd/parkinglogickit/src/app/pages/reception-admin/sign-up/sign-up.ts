import { Component, ChangeDetectorRef, NgZone, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RestServer } from '../../../../Rest/RestServer';
import { Driver } from '../../../../Auth/Driver';
import { Router } from '@angular/router';
import { Capacitor } from '@capacitor/core';
import { AuthService } from '../../../../Auth/auth.service'; // Import requis pour la sécurité
import { Md5 } from 'ts-md5';

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './sign-up.html',
  styleUrls: ['./sign-up.css'],
})
export class SignUp implements OnInit {
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
  showPassword: boolean = false;

  constructor(
    private restServer: RestServer,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    private authService: AuthService // Injection du service de sécurité
  ) {}

  async ngOnInit(): Promise<void> {
    // BARRIÈRE DE SÉCURITÉ : Contrôle d'accès strict au rôle Admin
    // (Nécessaire car cet écran permet de créer des comptes "Admin")
    const currentRole = await this.authService.getRole();
    const currentUsername = await this.authService.getUsername();

    if (!currentUsername || currentRole !== 'Admin') {
      console.warn('Tentative d’accès non autorisé à l’écran de création d’utilisateurs.');
      this.authService.logout();
      this.router.navigate(['/sign-in']);
      return;
    }
  }

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

    // BARRIÈRE DE SÉCURITÉ 2 : Politique de force du mot de passe
    // Exige : 8 caractères min, 1 majuscule, 1 minuscule, 1 chiffre, 1 caractère spécial
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    if (!passwordRegex.test(this.password)) {
      this.setMessage(
        'Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial (@$!%*?&).',
        'error'
      );
      return;
    }

    // ASSAINISSEMENT DES ENTRÉES : Filtrage anti-injection (XSS / SQL)
    const sanitizedFirstname = this.firstname.trim().replace(/[<>"/\\;`]/g, '');
    const sanitizedLastname = this.lastName.trim().replace(/[<>"/\\;`]/g, '');
    const sanitizedUsername = this.username.trim().replace(/[<>"/\\;`\s]/g, ''); // Pas d'espaces dans l'identifiant

    this.isLoading = true;
    this.message = '';

    const DriverClass =
      this.DriverType === 0
        ? 'lml.snir.parkinglogickit.metier.entity.Admin'
        : 'lml.snir.parkinglogickit.metier.entity.Driver';

    const DriverData: any = {
      firstName: sanitizedFirstname,
      lastName: sanitizedLastname,
      username: sanitizedUsername,
      password: Md5.hashStr(this.password),
      age: Number(this.age),
      isMale: Boolean(this.isMale),
      class: DriverClass,
    };

    this.restServer
      .getDriverService()
      .add(DriverData as Driver)
      .subscribe({
        next: (createdDriver: any) => {
          this.ngZone.run(async () => {
            this.isLoading = false;
            this.setMessage('Inscription réussie', 'success');

            // Stockage temporaire sécurisé du conducteur créé pour l'écran suivant (add-vehicle)
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
            this.setMessage(
              error?.error?.message || "Une erreur s'est produite lors de la création",
              'error'
            );
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

  generateMdp(): void {
    this.password = this.generateRandomPassword();
    this.showPassword = true;
  }

  copyMdp(): void {
    if (!this.password) {
      this.setMessage('Veuillez générer ou saisir un mot de passe avant de le copier.', 'error');
      return;
    }

    if (navigator.clipboard?.writeText) {
      navigator.clipboard
        .writeText(this.password)
        .then(() => this.setMessage('Le mot de passe a été copié dans le presse-papier.', 'success'))
        .catch(() => this.fallbackCopyText(this.password));
      return;
    }

    this.fallbackCopyText(this.password);
  }

  private generateRandomPassword(length: number = 12): string {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*';
    const array = new Uint32Array(length);
    window.crypto.getRandomValues(array);

    return Array.from(array, (value) => chars.charAt(value % chars.length)).join('');
  }

  private fallbackCopyText(text: string): void {
    try {
      const textArea = document.createElement('textarea');
      textArea.value = text;
      textArea.style.position = 'fixed';
      document.body.appendChild(textArea);
      textArea.focus();
      textArea.select();
      const successful = document.execCommand('copy');
      document.body.removeChild(textArea);
      this.setMessage(
        successful
          ? 'Le mot de passe a été copié dans le presse-papier.'
          : 'Échec de la copie du mot de passe.',
        successful ? 'success' : 'error'
      );
    } catch {
      this.setMessage('Échec de la copie du mot de passe.', 'error');
    }
  }
}
