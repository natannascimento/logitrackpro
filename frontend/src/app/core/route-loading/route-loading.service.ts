import { Injectable, inject, signal } from '@angular/core';
import {
  NavigationCancel,
  NavigationEnd,
  NavigationError,
  NavigationStart,
  Router,
} from '@angular/router';

const DURACAO_MINIMA_MS = 600;

@Injectable({ providedIn: 'root' })
export class RouteLoadingService {
  private readonly router = inject(Router);
  private readonly _loading = signal(false);
  readonly loading = this._loading.asReadonly();

  private inicioNavegacao = 0;

  constructor() {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationStart) {
        this.inicioNavegacao = Date.now();
        this._loading.set(true);
        return;
      }

      if (
        event instanceof NavigationEnd ||
        event instanceof NavigationCancel ||
        event instanceof NavigationError
      ) {
        const decorrido = Date.now() - this.inicioNavegacao;
        const restante = DURACAO_MINIMA_MS - decorrido;
        if (restante > 0) {
          setTimeout(() => this._loading.set(false), restante);
        } else {
          this._loading.set(false);
        }
      }
    });
  }
}
