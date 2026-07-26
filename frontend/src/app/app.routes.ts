import { Routes } from '@angular/router';
import { Viagens } from './features/viagens/viagens';
import { Dashboard } from './features/dashboard/dashboard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'viagens' },
  { path: 'viagens', component: Viagens },
  { path: 'dashboard', component: Dashboard },
];
