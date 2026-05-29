import { bootstrapApplication } from '@angular/platform-browser';
import { App } from './app/app';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { routes } from './app/app.routes';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeuix/themes/aura';
import { authInterceptor } from '../src/Auth/auth.interceptor.js';
bootstrapApplication(App, {
  providers: [
    provideRouter(routes),

    // SÉCURISATION : Branchement de l'intercepteur pour injecter automatiquement le token JWT
    provideHttpClient(withFetch(), withInterceptors([authInterceptor])),

    providePrimeNG({
      theme: {
        preset: Aura,
      },
    }),
  ],
}).catch((err) => console.error(err));
