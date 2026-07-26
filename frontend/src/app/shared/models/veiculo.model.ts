export type CategoriaVeiculo = 'LEVE' | 'PESADO';

export interface Veiculo {
  id: number;
  placa: string;
  modelo: string;
  tipo: CategoriaVeiculo;
  ano: number;
}
