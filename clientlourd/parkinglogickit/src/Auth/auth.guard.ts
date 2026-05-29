import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = async () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Verification asynchrone de l'etat de la session
  const loggedIn = await authService.isLoggedIn();

  if (loggedIn) {
    // Le passage est autorise. La gestion du timeout est laissee
    // a l'ecouteur d'activite global pour eviter les doublons.
    return true;
  }

  // Redirection defensive vers l'ecran de connexion
  router.navigate(['/sign-in']);
  return false;
};
