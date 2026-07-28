import { Routes } from '@angular/router';
import { Viagens } from './features/viagens/viagens';
import { Dashboard } from './features/dashboard/dashboard';
import { Login } from './features/login/login';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'viagens' },
  { path: 'login', component: Login },
  { path: 'viagens', component: Viagens },
  { path: 'dashboard', component: Dashboard },
];
