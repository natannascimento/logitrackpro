import { Routes } from '@angular/router';
import { Viagens } from './features/viagens/viagens';
import { Dashboard } from './features/dashboard/dashboard';
import { Login } from './features/login/login';
import { authGuard } from './core/auth/auth-guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'viagens' },
  { path: 'login', component: Login },
  { path: 'viagens', component: Viagens, canActivate: [authGuard] },
  { path: 'dashboard', component: Dashboard, canActivate: [authGuard] },
];
