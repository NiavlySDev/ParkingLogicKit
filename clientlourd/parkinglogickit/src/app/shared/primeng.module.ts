import { NgModule } from '@angular/core';
// On retire RouterLink d'ici

import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { CheckboxModule } from 'primeng/checkbox';
import { ListboxModule } from 'primeng/listbox';
import { MessageService } from 'primeng/api';

const PRIMENG_COMPONENTS = [
  ButtonModule,
  InputTextModule,
  TableModule,
  DialogModule,
  CardModule,
  ToastModule,
  CheckboxModule,
  ListboxModule,
];

@NgModule({
  imports: [...PRIMENG_COMPONENTS],
  exports: [...PRIMENG_COMPONENTS],
  providers: [ MessageService]
})
export class PrimengModule {}
