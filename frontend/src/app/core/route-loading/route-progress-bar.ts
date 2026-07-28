import { Component, inject } from '@angular/core';
import { RouteLoadingService } from './route-loading.service';

@Component({
  selector: 'app-route-progress-bar',
  imports: [],
  templateUrl: './route-progress-bar.html',
  styleUrl: './route-progress-bar.scss',
})
export class RouteProgressBar {
  protected readonly loading = inject(RouteLoadingService).loading;
}
