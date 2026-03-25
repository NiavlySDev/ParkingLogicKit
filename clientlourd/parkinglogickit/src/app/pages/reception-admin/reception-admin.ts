// reception-admin.component.ts
import { MenuItem } from 'primeng/api';

export class ReceptionAdmin {
  username = 'Admin';

  menuItems: MenuItem[] = [
    { label: 'Accueil',      icon: 'pi pi-home',        routerLink: ['/accueil'] },
    { label: 'Dashboard',    icon: 'pi pi-chart-bar',   routerLink: ['/dashboard'] },
    { label: 'Journal',      icon: 'pi pi-book',        routerLink: ['/journal'] },
    { separator: true },
    { label: 'Conducteurs',  icon: 'pi pi-users',       routerLink: ['/conducteurs'] },
    { label: 'Vehicules',    icon: 'pi pi-car',         routerLink: ['/vehicules'] },
    { label: 'Badges',       icon: 'pi pi-id-card',     routerLink: ['/badges'] },
    { separator: true },
    { label: 'Compte',       icon: 'pi pi-user',        routerLink: ['/compte'] },
  ];
}