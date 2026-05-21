import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';
import { catchError, throwError, from, switchMap } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  // from(...) transforme la Promise<string | null> en un Observable
  return from(authService.getToken()).pipe(
    switchMap(token => {
      let securedReq = req;

      // Si le token existe, on clone la requête pour y injecter le header
      if (token) {
        securedReq = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        });
      }

      // On passe la requête au backend Java
      return next(securedReq);
    }),
    catchError((error: HttpErrorResponse) => {
      // Détection des tokens expirés ou invalides (401 / 403)
      if (error.status === 401 || error.status === 403) {
        console.warn('[SECURITY ALERT] Session expirée ou jeton invalide.');
        authService.logout();
      }
      return throwError(() => error);
    })
  );
};