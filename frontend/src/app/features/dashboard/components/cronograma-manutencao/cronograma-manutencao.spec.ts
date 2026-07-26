import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LOCALE_ID } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';

import { CronogramaManutencao } from './cronograma-manutencao';
import { Manutencao } from '../../../../shared/models/manutencao.model';

registerLocaleData(localePt);

const MANUTENCAO_MOCK: Manutencao = {
  id: 1,
  veiculo: { id: 1, placa: 'ABC-1234', modelo: 'Fiorino', tipo: 'LEVE', ano: 2022 },
  dataInicio: '2024-06-10',
  dataFinalizacao: '2024-06-11',
  tipoServico: 'Troca de Óleo',
  custoEstimado: 350,
  status: 'PENDENTE',
};

describe('CronogramaManutencao', () => {
  let fixture: ComponentFixture<CronogramaManutencao>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CronogramaManutencao],
      providers: [{ provide: LOCALE_ID, useValue: 'pt-BR' }],
    }).compileComponents();

    fixture = TestBed.createComponent(CronogramaManutencao);
    fixture.componentRef.setInput('manutencoes', [MANUTENCAO_MOCK]);
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should render a table row per manutencao', () => {
    fixture.detectChanges();
    const linhas = fixture.nativeElement.querySelectorAll('tr.mat-mdc-row');
    expect(linhas.length).toBe(1);
    expect(linhas[0].textContent).toContain('ABC-1234');
    expect(linhas[0].textContent).toContain('Troca de Óleo');
    expect(linhas[0].textContent).toContain('PENDENTE');
  });
});
