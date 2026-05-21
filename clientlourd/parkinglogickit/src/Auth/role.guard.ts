import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const RoleGuard: CanActivateFn = async (route) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const expectedRole = route.data?.['role'];
  const userRole = await authService.getRole();

  if (userRole === expectedRole) {
    authService.resetTimeout();
    return true;
  }

  router.navigate(['/reception']);
  return false;
};