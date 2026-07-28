import { Component, computed, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter, map } from 'rxjs';
import { Navbar } from './shared/components/navbar/navbar';
import { RouteProgressBar } from './core/route-loading/route-progress-bar';
import { RouteSkeleton } from './core/route-loading/route-skeleton';
import { RouteLoadingService } from './core/route-loading/route-loading.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Navbar, RouteProgressBar, RouteSkeleton],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  private readonly router = inject(Router);

  private readonly url = toSignal(
    this.router.events.pipe(
      filter((event) => event instanceof NavigationEnd),
      map((event) => event.urlAfterRedirects),
    ),
    { initialValue: this.router.url },
  );

  protected readonly showShell = computed(() => !this.url().startsWith('/login'));
  protected readonly loading = inject(RouteLoadingService).loading;
}
