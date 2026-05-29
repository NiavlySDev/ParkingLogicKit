import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule, NgClass } from '@angular/common';
import { DriverService } from '../../../Rest/DriverService';
import { Router } from '@angular/router';
import { PrimengModule } from '../../shared/primeng.module';
import { MessageService } from 'primeng/api';
import { AuthService } from '../../../Auth/auth.service';
import { UpdateCheckService } from '../../services/update-check.service';

@Component({
  selector: 'app-sign-in',
  standalone: true,
  imports: [FormsModule, CommonModule, PrimengModule, NgClass],
  templateUrl: './sign-in.html',
  styleUrls: ['./sign-in.css'],
  providers: [MessageService],
})
export class SignIn implements OnInit {
  username: string = '';
  password: string = '';
  isLoading: boolean = false;
  message: string = '';
  messageType: 'success' | 'error' = 'success';
  showPassword: boolean = false;
  appVersion: string = '';

  constructor(
    private driverService: DriverService,
    private router: Router,
    private messageService: MessageService,
    private cdr: ChangeDetectorRef,
    private authService: AuthService,
    private updateCheckService: UpdateCheckService
  ) {}

  async ngOnInit(): Promise<void> {
    this.appVersion = await this.updateCheckService.getCurrentVersion();
    this.cdr.detectChanges();
  }

  goHome(): void {
    this.router.navigate(['/']);
  }

  goSettings(): void {
    this.router.navigate(['/settings']);
  }

  async onSubmit(): Promise<void> {
    // 1. Barrière Réseau
    if (!navigator.onLine) {
      this.setMessage('Connexion impossible : Aucun accès internet détecté.', 'error');
      return;
    }

    // 2. Validation des présences
    if (!this.username || !this.password) {
      this.setMessage('Identifiant et mot de passe obligatoires.', 'error');
      return;
    }

    this.isLoading = true;
    this.message = '';

    // ASSAINISSEMENT : Élimination des caractères d'injection sur l'identifiant
    const sanitizedUsername = this.username.trim().replace(/[<>"/\\;`\s]/g, '');

    this.driverService.getByUsername(sanitizedUsername).subscribe({
      next: async (driver: any) => {
        // NOTE SÉCURITÉ : Idéalement, cette vérification se fait sur le backend Java via POST
        if (driver && driver.password === this.password) {
          // Détermination prudente du rôle applicatif
          const userRole = driver.class && driver.class.includes('Admin') ? 'Admin' : 'Driver';

          const tokenPayload = {
            username: driver.username,
            role: userRole,
            exp: Math.floor(Date.now() / 1000) + 60 * 60, // Expiration 1 heure
          };

          // Construction temporaire du Jeton (À remplacer par un retour d'API Backend signé)
          const token =
            btoa(JSON.stringify({ alg: 'HS256', typ: 'JWT' })) +
            '.' +
            btoa(JSON.stringify(tokenPayload)) +
            '.' +
            btoa('temporary_local_signature');

          await this.authService.setToken(token);
          const isAdminRole = await this.authService.isAdmin();

          this.isLoading = false;

          // Redirection étanche selon les privilèges vérifiés
          if (isAdminRole && userRole === 'Admin') {
            this.router.navigate(['/reception-admin']);
          } else {
            this.router.navigate(['/reception']);
          }
        } else {
          this.isLoading = false;
          // ANTI-ÉNUMÉRATION : Message flou pour ne pas aider un attaquant
          this.setMessage('Identifiant ou mot de passe incorrect.', 'error');
        }
      },
      error: (err) => {
        this.isLoading = false;
        // ANTI-ÉNUMÉRATION : Même message si l'utilisateur n'existe pas du tout
        this.setMessage('Identifiant ou mot de passe incorrect.', 'error');
      },
    });
  }

  private setMessage(message: string, type: 'success' | 'error'): void {
    this.message = message;
    this.messageType = type;
    this.cdr.detectChanges();
  }
}
