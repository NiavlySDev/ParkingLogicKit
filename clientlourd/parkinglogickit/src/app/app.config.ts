import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http'; // <-- Ajout de withInterceptors

import { routes } from './app.routes';
import { authInterceptor } from '../Auth/auth.interceptor'; // <-- Import de ton nouvel intercepteur

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(), 
    provideRouter(routes), 
    provideHttpClient(
      withInterceptors([authInterceptor]) // <-- La sécurité est injectée ici pour toutes les requêtes HTTP
    )
  ],
};