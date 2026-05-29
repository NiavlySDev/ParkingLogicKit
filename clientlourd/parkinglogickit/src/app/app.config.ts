import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';
import { authInterceptor } from '../Auth/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(
      // L'intercepteur de securite doit imperativement rester en premiere position
      // pour garantir le hachage et l'injection du token sur le flux HTTP sortant.
      withInterceptors([authInterceptor])
    ),
  ],
};
