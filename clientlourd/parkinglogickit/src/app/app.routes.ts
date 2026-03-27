import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { SignIn } from './pages/sign-in/sign-in';
import { SignUp } from './pages/sign-up/sign-up';
import { Reception } from './pages/reception/reception';
import { ReceptionAdmin } from './pages/reception-admin/reception-admin';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'sign-in', component: SignIn },
  { path: 'sign-up', component: SignUp },
  { path: 'reception', component: Reception },
  { path: 'reception-admin', component: ReceptionAdmin },
  { path: '**', redirectTo: '' },
];
