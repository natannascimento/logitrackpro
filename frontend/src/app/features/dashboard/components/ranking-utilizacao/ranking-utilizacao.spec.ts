import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RankingUtilizacao } from './ranking-utilizacao';

describe('RankingUtilizacao', () => {
  let fixture: ComponentFixture<RankingUtilizacao>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RankingUtilizacao],
    }).compileComponents();

    fixture = TestBed.createComponent(RankingUtilizacao);
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should show a fallback message when there is no ranking yet', () => {
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('Nenhuma viagem registrada ainda.');
  });

  it('should render the vehicle with the highest accumulated km', () => {
    fixture.componentRef.setInput('ranking', {
      veiculoId: 2,
      placa: 'XYZ-9876',
      modelo: 'Volvo FH',
      tipo: 'PESADO',
      ano: 2021,
      totalKm: 1000,
    });
    fixture.detectChanges();

    const texto = fixture.nativeElement.textContent;
    expect(texto).toContain('XYZ-9876');
    expect(texto).toContain('1000');
  });
});
