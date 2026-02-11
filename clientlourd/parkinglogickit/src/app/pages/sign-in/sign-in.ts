import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { DriverService } from '../../../Rest/DriverService';
import { Driver } from '../../../Auth/Driver';
import { Router } from '@angular/router';

// author Ethan

@Component({
  selector: 'app-sign-in',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './sign-in.html',
  styleUrls: ['./sign-in.css'],
})
export class SignIn {
  login: string = '';
  password: string = '';

  isLoading: boolean = false;
  message: string = '';
  messageType: 'success' | 'error' = 'success';

  constructor(private driverService: DriverService, private router: Router) {}

  // Bouton Home
  goHome(): void {
    console.log('Redirection vers Home');
    this.router.navigate(['/']); // redirection vers la page d'accueil
  }

  // Soumission du formulaire
  onSubmit(): void {
    if (!this.login || !this.password) {
      this.setMessage('Tous les champs sont obligatoires', 'error');
      return;
    }

    this.isLoading = true;
    this.message = '';
    console.log('Login envoyé :', this.login);

    this.driverService.getByLogin(this.login).subscribe({
      next: (driver: Driver) => {
        console.log('Réponse du serveur :', driver);
        this.isLoading = false;
        if (driver.password === this.password) {
          this.setMessage('Connexion réussie 🎉', 'success');
          // Redirection automatique après succès
          setTimeout(() => {
            this.router.navigate(['/']);
          }, 500); // délai pour voir le message
        } else {
          this.setMessage('Mot de passe incorrect', 'error');
        }
      },
      error: (err) => {
        console.error('Erreur serveur :', err);
        this.isLoading = false;
        this.setMessage('Utilisateur introuvable', 'error');
      },
    });
  }

  private setMessage(message: string, type: 'success' | 'error'): void {
    this.message = message;
    this.messageType = type;
  }
}
