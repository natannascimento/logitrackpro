import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LOCALE_ID } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';

import { Dashboard } from './dashboard';
import { environment } from '../../../environments/environment';
import { Dashboard as DashboardModel } from '../../shared/models/dashboard.model';

registerLocaleData(localePt);

const DASHBOARD_MOCK: DashboardModel = {
  totalKmPercorrido: 1455.5,
  volumePorCategoria: [
    { categoria: 'LEVE', quantidade: 2 },
    { categoria: 'PESADO', quantidade: 1 },
  ],
  cronogramaManutencoes: [
    {
      id: 1,
      veiculo: { id: 1, placa: 'ABC-1234', modelo: 'Fiorino', tipo: 'LEVE', ano: 2022 },
      dataInicio: '2024-06-10',
      dataFinalizacao: '2024-06-11',
      tipoServico: 'Troca de Óleo',
      custoEstimado: 350,
      status: 'PENDENTE',
    },
  ],
  rankingUtilizacao: {
    veiculoId: 2,
    placa: 'XYZ-9876',
    modelo: 'Volvo FH',
    tipo: 'PESADO',
    ano: 2021,
    totalKm: 1000,
  },
  projecaoFinanceiraMesCorrente: 350,
};

describe('Dashboard', () => {
  let component: Dashboard;
  let fixture: ComponentFixture<Dashboard>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Dashboard],
      providers: [provideHttpClient(), provideHttpClientTesting(), { provide: LOCALE_ID, useValue: 'pt-BR' }],
    }).compileComponents();

    fixture = TestBed.createComponent(Dashboard);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should create', () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/dashboard`).flush(DASHBOARD_MOCK);
    httpMock.expectOne(`${environment.apiUrl}/veiculos`).flush([]);
    expect(component).toBeTruthy();
  });

  it('should render the 5 metrics: KPI cards, volume-per-category table, ranking and cronograma', async () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/dashboard`).flush(DASHBOARD_MOCK);
    httpMock.expectOne(`${environment.apiUrl}/veiculos`).flush([]);
    await fixture.whenStable();
    fixture.detectChanges();

    const texto = fixture.nativeElement.textContent;
    expect(texto).toContain('1.455,50');
    expect(texto).toContain('LEVE');
    expect(texto).toContain('PESADO');
    expect(texto).toContain('XYZ-9876');
    expect(texto).toContain('Troca de Óleo');
  });

  it('should reload the metrics filtered by the selected vehicle', async () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/dashboard`).flush(DASHBOARD_MOCK);
    httpMock.expectOne(`${environment.apiUrl}/veiculos`).flush([]);
    await fixture.whenStable();
    fixture.detectChanges();

    component.filtrarPorVeiculo(2);

    const req = httpMock.expectOne((request) => request.url === `${environment.apiUrl}/dashboard`);
    expect(req.request.params.get('veiculoId')).toBe('2');
    req.flush(DASHBOARD_MOCK);
  });
});
