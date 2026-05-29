import { Routes } from '@angular/router';

import { Home } from './pages/home/home';
import { SignIn } from './pages/sign-in/sign-in';
import { Reception } from './pages/reception/reception';
import { UserProfile } from './pages/user-profile/user-profile';
import { Settings } from './pages/settings/settings';

import { ReceptionAdmin } from './pages/reception-admin/reception-admin';
import { SignUp } from './pages/reception-admin/sign-up/sign-up';
import { DeleteUser } from './pages/reception-admin/delete-user/delete-user';
import { ModifyUser } from './pages/reception-admin/modify-user/modify-user';
import { AddVehicle } from './pages/reception-admin/add-vehicle/add-vehicle';
import { ModifyVehicle } from './pages/reception-admin/modify-vehicle/modify-vehicle';
import { DeleteVehicle } from './pages/reception-admin/delete-vehicle/delete-vehicle';

import { authGuard } from '../Auth/auth.guard';
import { RoleGuard } from '../Auth/role.guard';

export const routes: Routes = [
  // Routes publiques
  { path: '', component: Home },
  { path: 'sign-in', component: SignIn },

  // Routes protegees (Tous les utilisateurs connectes)
  {
    path: 'reception',
    component: Reception,
    canActivate: [authGuard],
  },
  {
    path: 'user-profile',
    component: UserProfile,
    canActivate: [authGuard],
  },
  {
    path: 'settings',
    component: Settings,
    canActivate: [authGuard],
  },
  {
    path: 'add-vehicle',
    component: AddVehicle,
    canActivate: [authGuard], // Permet l'accès au conducteur cree pour finaliser son inscription
  },

  // Routes d'administration (Admin uniquement)
  {
    path: 'reception-admin',
    component: ReceptionAdmin,
    canActivate: [authGuard, RoleGuard],
    data: { role: 'Admin' },
  },
  {
    path: 'sign-up',
    component: SignUp,
    canActivate: [authGuard, RoleGuard],
    data: { role: 'Admin' },
  },
  {
    path: 'modify-user',
    component: ModifyUser,
    canActivate: [authGuard, RoleGuard],
    data: { role: 'Admin' },
  },
  {
    path: 'delete-user',
    component: DeleteUser,
    canActivate: [authGuard, RoleGuard],
    data: { role: 'Admin' },
  },
  {
    path: 'modify-vehicle',
    component: ModifyVehicle,
    canActivate: [authGuard, RoleGuard],
    data: { role: 'Admin' },
  },
  {
    path: 'delete-vehicle',
    component: DeleteVehicle,
    canActivate: [authGuard, RoleGuard],
    data: { role: 'Admin' },
  },

  // Redirection par defaut pour les URLs inconnues
  { path: '**', redirectTo: '' },
];
