import { Veiculo } from './veiculo.model';

export interface Viagem {
  id: number;
  veiculo: Veiculo;
  dataSaida: string;
  dataChegada: string | null;
  origem: string | null;
  destino: string | null;
  kmPercorrida: number;
  createdAt: string;
  updatedAt: string;
}

export interface ViagemRequest {
  veiculoId: number;
  dataSaida: string;
  dataChegada: string | null;
  origem: string | null;
  destino: string | null;
  kmPercorrida: number;
}
