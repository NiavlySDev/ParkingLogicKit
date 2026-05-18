import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRole = route.data['role'];
    const userRole = this.authService.getRole();
    console.log('RoleGuard - role attendu:', expectedRole, '- role user:', userRole);
    if (userRole === expectedRole) {
      this.authService.resetTimeout();
      return true;
    }
    this.router.navigate(['/reception']);
    return false;
  }
}
