import { Manutencao } from './manutencao.model';

export interface VolumePorCategoria {
  categoria: string;
  quantidade: number;
}

export interface RankingUtilizacao {
  veiculoId: number;
  placa: string;
  modelo: string;
  tipo: string;
  ano: number;
  totalKm: number;
}

export interface Dashboard {
  totalKmPercorrido: number;
  volumePorCategoria: VolumePorCategoria[];
  cronogramaManutencoes: Manutencao[];
  rankingUtilizacao: RankingUtilizacao | null;
  projecaoFinanceiraMesCorrente: number;
}
