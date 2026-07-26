import { Component, OnInit, computed, inject } from '@angular/core';
import { DecimalPipe, CurrencyPipe } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';

import { DashboardService } from './services/dashboard.service';
import { KpiCard } from '../../shared/components/kpi-card/kpi-card';
import { RankingUtilizacao } from './components/ranking-utilizacao/ranking-utilizacao';
import { CronogramaManutencao } from './components/cronograma-manutencao/cronograma-manutencao';
import { ViagemService } from '../viagens/services/viagem.service';

@Component({
  selector: 'app-dashboard',
  imports: [
    MatTableModule,
    MatSelectModule,
    MatFormFieldModule,
    KpiCard,
    DecimalPipe,
    CurrencyPipe,
    RankingUtilizacao,
    CronogramaManutencao,
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit {
  private readonly dashboardService = inject(DashboardService);
  private readonly viagemService = inject(ViagemService);

  protected readonly dashboard = computed(() => this.dashboardService.dashboard());
  protected readonly veiculos = this.viagemService.veiculos;
  protected readonly colunasVolumePorCategoria = ['categoria', 'quantidade'];

  ngOnInit(): void {
    this.dashboardService.carregarMetricas();
    this.viagemService.carregarVeiculos();
  }

  filtrarPorVeiculo(veiculoId: number | null): void {
    this.dashboardService.carregarMetricas(veiculoId ?? undefined);
  }
}
