import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

import { environment } from '../../../../environments/environment';
import { Dashboard } from '../../../shared/models/dashboard.model';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/dashboard`;

  readonly dashboard = signal<Dashboard | null>(null);

  async carregarMetricas(veiculoId?: number): Promise<void> {
    const params = veiculoId ? new HttpParams().set('veiculoId', veiculoId) : undefined;
    const dashboard = await firstValueFrom(this.http.get<Dashboard>(this.apiUrl, { params }));
    this.dashboard.set(dashboard);
  }
}
