import { NgModule } from '@angular/core';
import { RouterLink } from '@angular/router';

// Les essentiels pour démarrer
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { CheckboxModule } from 'primeng/checkbox';

const PRIMENG_MODULES = [
  ButtonModule,
  InputTextModule,
  TableModule, // Tableau de datas
  DialogModule, // Dialogues modaux
  CardModule, // Cartes d'affichage
  RouterLink, // Pour les liens de routage
  ToastModule, // Notifications toast
  CheckboxModule, // Cases à cocher
  // ... liste tous ceux que tu veux
];

@NgModule({
  imports: PRIMENG_MODULES,
  exports: PRIMENG_MODULES,
})
export class PrimengModule {}
