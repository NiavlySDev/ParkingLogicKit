import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const RoleGuard: CanActivateFn = async (route) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const expectedRole = route.data?.['role'];
  const userRole = await authService.getRole();

  // Si l'utilisateur possède exactement le rôle requis, le passage est autorisé
  if (userRole === expectedRole) {
    // La gestion du timeout est laissée à l'écouteur d'activité global
    return true;
  }

  // Sécurisation de la redirection : si l'utilisateur n'est même pas connecté
  if (!userRole) {
    router.navigate(['/sign-in']);
  } else {
    // Si l'utilisateur est connecté mais n'a pas les droits requis (ex: Driver qui tente d'aller sur l'admin)
    router.navigate(['/reception']);
  }

  return false;
};
