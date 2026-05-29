import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';
import { catchError, throwError, from, switchMap } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const isTargetingBackend = req.url.includes('/api/') || req.url.startsWith('http://localhost');

  // Transformation de la Promise en Observable pour recuperer le jeton
  return from(authService.getToken()).pipe(
    switchMap((token) => {
      let securedReq = req;

      // SECURISATION : On injecte le token uniquement si la requete cible notre API centrale
      // (Cela evite d'envoyer le secret a des services externes de cartographie, meteo, etc.)
      if (token && isTargetingBackend) {
        securedReq = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
          },
        });
      }

      return next(securedReq);
    }),
    catchError((error: HttpErrorResponse) => {
      // Detection des jetons expires ou invalides (401 / 403)
      if (isTargetingBackend && (error.status === 401 || error.status === 403)) {
        console.warn('Session expiree ou jeton invalide. Nettoyage de la session.');

        // SECURISATION : Suppression immediate locale pour couper court a toute boucle infinie
        authService.logout();
      }
      return throwError(() => error);
    })
  );
};
