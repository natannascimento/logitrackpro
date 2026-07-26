import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

import { environment } from '../../../../environments/environment';
import { Viagem, ViagemRequest } from '../../../shared/models/viagem.model';
import { Veiculo } from '../../../shared/models/veiculo.model';

@Injectable({ providedIn: 'root' })
export class ViagemService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/viagens`;
  private readonly veiculosApiUrl = `${environment.apiUrl}/veiculos`;

  readonly viagens = signal<Viagem[]>([]);
  readonly veiculos = signal<Veiculo[]>([]);

  async carregarViagens(): Promise<void> {
    const viagens = await firstValueFrom(this.http.get<Viagem[]>(this.apiUrl));
    this.viagens.set(viagens);
  }

  async carregarVeiculos(): Promise<void> {
    const veiculos = await firstValueFrom(this.http.get<Veiculo[]>(this.veiculosApiUrl));
    this.veiculos.set(veiculos);
  }

  async criar(request: ViagemRequest): Promise<Viagem> {
    return firstValueFrom(this.http.post<Viagem>(this.apiUrl, request));
  }

  async atualizar(id: number, request: ViagemRequest): Promise<Viagem> {
    return firstValueFrom(this.http.put<Viagem>(`${this.apiUrl}/${id}`, request));
  }

  async excluir(id: number): Promise<void> {
    await firstValueFrom(this.http.delete<void>(`${this.apiUrl}/${id}`));
  }
}
