import { Component, input } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { MatTableModule } from '@angular/material/table';

import { Manutencao } from '../../../../shared/models/manutencao.model';

@Component({
  selector: 'app-cronograma-manutencao',
  imports: [MatTableModule, DatePipe, CurrencyPipe],
  templateUrl: './cronograma-manutencao.html',
  styleUrl: './cronograma-manutencao.scss',
})
export class CronogramaManutencao {
  readonly manutencoes = input.required<Manutencao[]>();

  protected readonly colunas = ['veiculo', 'tipoServico', 'dataInicio', 'custoEstimado', 'status'];
}
