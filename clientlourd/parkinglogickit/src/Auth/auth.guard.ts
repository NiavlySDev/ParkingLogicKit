import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = async () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const loggedIn = await authService.isLoggedIn();

  if (loggedIn) {
    authService.resetTimeout();
    return true;
  }

  router.navigate(['/sign-in']);
  return false;
};