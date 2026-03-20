import { Component, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { DriverService } from '../../../Rest/DriverService';
import { Driver } from '../../../Auth/Driver';
import { Router } from '@angular/router';
import { PrimengModule } from '../../shared/primeng.module';
import { MessageService } from 'primeng/api';
// author Ethan
@Component({
  selector: 'app-sign-in',
  standalone: true,
  imports: [FormsModule, CommonModule, PrimengModule],
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
  constructor(
    private driverService: DriverService,
    private router: Router,
    private messageService: MessageService,
    private cdr: ChangeDetectorRef
  ) {}
  // Bouton Home
  goHome(): void {
    this.router.navigate(['/']);
  }
  // Soumission du formulaire
  onSubmit(): void {
    if (!this.username && !this.password) {
      this.message = "Nom d'utilisateur et mot de passe obligatoires";
      this.messageType = 'error';
      this.cdr.detectChanges();
      return;
    }
    if (!this.username) {
      this.message = "Nom d'utilisateur obligatoire";
      this.messageType = 'error';
      this.cdr.detectChanges();
      return;
    }
    if (!this.password) {
      this.message = 'Mot de passe obligatoire';
      this.messageType = 'error';
      this.cdr.detectChanges();
      return;
    }
    this.isLoading = true;
    this.message = '';
    this.driverService.getByUsername(this.username).subscribe({
      next: (driver: Driver) => {
        this.isLoading = false;
        if (driver.password === this.password) {
          if (driver.class.includes('Admin')) {
            this.router.navigate(['/reception-admin'], {
              queryParams: { username: this.username },
            });
          } else {
            this.router.navigate(['/reception'], {
              queryParams: { username: this.username },
            });
          }
        } else {
          this.message = 'Mot de passe incorrect';
          this.messageType = 'error';
          this.cdr.detectChanges();
        }
      },
      error: (err) => {
        //console.error('Erreur serveur :', err);
        this.isLoading = false;
        this.message = 'Utilisateur introuvable';
        this.messageType = 'error';
        this.cdr.detectChanges();
      },
    });
  }
  private setMessage(message: string, type: 'success' | 'error'): void {
    this.message = message;
    this.messageType = type;
  }
}
