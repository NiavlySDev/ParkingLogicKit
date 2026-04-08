import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { SignIn } from './pages/sign-in/sign-in';
import { SignUp } from './pages/reception-admin/sign-up/sign-up';
import { Reception } from './pages/reception/reception';
import { ReceptionAdmin } from './pages/reception-admin/reception-admin';

import { AuthGuard } from '../Auth/auth.guard';
import { RoleGuard } from '../Auth/role.guard';

import { DeleteUser } from './pages/reception-admin/delete-user/delete-user';
import { ModifyUser } from './pages/reception-admin/modify-user/modify-user';
import { DeleteVehicle } from './pages/reception-admin/delete-vehicle/delete-vehicle';
import { ModifyVehicle } from './pages/reception-admin/modify-vehicle/modify-vehicle';
import { AddVehicle } from './pages/reception-admin/add-vehicle/add-vehicle';

export const routes: Routes = [
  { path: '', component: Home },

  { path: 'sign-in', component: SignIn },

  { path: 'sign-up', component: SignUp },

  {
    path: 'reception',
    component: Reception,
    canActivate: [AuthGuard],
  },

  {
    path: 'reception-admin',
    component: ReceptionAdmin,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'Admin' },
  },

  {
    path: 'delete-user',
    component: DeleteUser,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'Admin' },
  },

  {
    path: 'modify-user',
    component: ModifyUser,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'Admin' },
  },

  {
    path: 'delete-vehicle',
    component: DeleteVehicle,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'Admin' },
  },

  {
    path: 'modify-vehicle',
    component: ModifyVehicle,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'Admin' },
  },

  {
    path: 'add-vehicle',
    component: AddVehicle,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'Admin' },
  },

  { path: '**', redirectTo: '' },
];
