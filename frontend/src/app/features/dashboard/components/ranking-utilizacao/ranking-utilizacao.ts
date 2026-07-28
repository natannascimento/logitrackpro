import { Component, input } from '@angular/core';

import { RankingUtilizacao as RankingUtilizacaoModel } from '../../../../shared/models/dashboard.model';

@Component({
  selector: 'app-ranking-utilizacao',
  imports: [],
  templateUrl: './ranking-utilizacao.html',
  styleUrl: './ranking-utilizacao.scss',
})
export class RankingUtilizacao {
  readonly ranking = input<RankingUtilizacaoModel | null>(null);
}
