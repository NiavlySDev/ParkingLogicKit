import { Component, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule, NgClass } from '@angular/common';
import { DriverService } from '../../../Rest/DriverService';
import { Router } from '@angular/router';
import { PrimengModule } from '../../shared/primeng.module';
import { MessageService } from 'primeng/api';
import { AuthService } from '../../../Auth/auth.service';

@Component({
  selector: 'app-sign-in',
  standalone: true,
  imports: [FormsModule, CommonModule, PrimengModule, NgClass],
  templateUrl: './sign-in.html',
  styleUrls: ['./sign-in.css'],
  providers: [MessageService],
})
export class SignIn {
  username: string = '';
  password: string = '';
  isLoading: boolean = false;
  message: string = '';
  messageType: 'success' | 'error' = 'success';

  // Variable pour piloter l'œil du mot de passe dans le HTML
  showPassword: boolean = false; 

  constructor(
    private driverService: DriverService,
    private router: Router,
    private messageService: MessageService,
    private cdr: ChangeDetectorRef,
    private authService: AuthService
  ) {}

  goHome(): void {
    this.router.navigate(['/']);
  }

  async onSubmit(): Promise<void> {
    // 1. BLOCAGE SI PAS INTERNET 🛡️
    if (!navigator.onLine) {
      this.setMessage("Connexion impossible : Vous n'êtes pas connecté à Internet. Veuillez vérifier votre réseau.", 'error');
      return;
    }

    // 2. Vérification des champs remplis
    if (!this.username && !this.password) {
      this.setMessage("Nom d'utilisateur et mot de passe obligatoires", 'error');
      return;
    }
    if (!this.username) {
      this.setMessage("Nom d'utilisateur obligatoire", 'error');
      return;
    }
    if (!this.password) {
      this.setMessage('Mot de passe obligatoire', 'error');
      return;
    }

    this.isLoading = true;
    this.message = '';

    this.driverService.getByUsername(this.username).subscribe({
      next: async (driver: any) => {
        if (driver.password === this.password) {
          const tokenPayload = {
            username: driver.username,
            role: driver.class.includes('Admin') ? 'Admin' : 'Driver',
            exp: Math.floor(Date.now() / 1000) + 60 * 60,
          };

          const token =
            btoa(JSON.stringify({})) +
            '.' +
            btoa(JSON.stringify(tokenPayload)) +
            '.' +
            btoa('signature');

          await this.authService.setToken(token);

          const isAdminRole = await this.authService.isAdmin();

          this.isLoading = false;

          // Redirection basée sur le rôle détecté
          if (isAdminRole) {
            this.router.navigate(['/reception-admin']);
          } else {
            this.router.navigate(['/reception']);
          }
        } else {
          this.isLoading = false;
          this.setMessage('Mot de passe incorrect', 'error');
        }
      },
      error: () => {
        this.isLoading = false;
        this.setMessage('Utilisateur introuvable', 'error');
      },
    });
  }

  private setMessage(message: string, type: 'success' | 'error'): void {
    this.message = message;
    this.messageType = type;
    this.cdr.detectChanges();
  }
}