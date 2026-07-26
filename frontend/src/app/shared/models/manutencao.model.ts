import { Veiculo } from './veiculo.model';

export type StatusManutencao = 'PENDENTE' | 'EM_REALIZACAO' | 'CONCLUIDA';

export interface Manutencao {
  id: number;
  veiculo: Veiculo;
  dataInicio: string;
  dataFinalizacao: string | null;
  tipoServico: string;
  custoEstimado: number;
  status: StatusManutencao;
}
