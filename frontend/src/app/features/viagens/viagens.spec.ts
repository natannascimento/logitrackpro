import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { MatDialog } from '@angular/material/dialog';

import { Viagens } from './viagens';
import { environment } from '../../../environments/environment';
import { Viagem } from '../../shared/models/viagem.model';

const VIAGEM_MOCK: Viagem = {
  id: 1,
  veiculo: { id: 1, placa: 'ABC-1234', modelo: 'Fiorino', tipo: 'LEVE', ano: 2022 },
  dataSaida: '2024-05-01T08:00:00',
  dataChegada: '2024-05-01T18:00:00',
  origem: 'São Paulo',
  destino: 'Rio de Janeiro',
  kmPercorrida: 435,
  createdAt: '2026-07-25T17:06:02.937433',
  updatedAt: '2026-07-25T17:06:02.937433',
};

describe('Viagens', () => {
  let component: Viagens;
  let fixture: ComponentFixture<Viagens>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Viagens],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(Viagens);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should create', () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/viagens`).flush([]);
    expect(component).toBeTruthy();
  });

  it('should render a table row per viagem returned by the API', async () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/viagens`).flush([VIAGEM_MOCK]);
    await fixture.whenStable();
    fixture.detectChanges();

    const linhas = fixture.nativeElement.querySelectorAll('tr.mat-mdc-row');
    expect(linhas.length).toBe(1);
    expect(linhas[0].textContent).toContain('ABC-1234');
    expect(linhas[0].textContent).toContain('Rio de Janeiro');
  });

  it('should open the form dialog and reload the list when "Nova viagem" is clicked and the dialog closes with a result', async () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/viagens`).flush([]);
    await fixture.whenStable();
    fixture.detectChanges();

    const botaoNovaViagem = fixture.nativeElement.querySelector('button');
    botaoNovaViagem.click();
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/veiculos`).flush([]);

    const dialogNoDom = document.querySelector('app-viagem-form-dialog');
    expect(dialogNoDom).toBeTruthy();

    const cancelarButton = Array.from(document.querySelectorAll('button')).find(
      (botao) => botao.textContent?.trim() === 'Cancelar',
    ) as HTMLButtonElement;
    cancelarButton.click();
    fixture.detectChanges();
    await fixture.whenStable();

    httpMock.expectNone(`${environment.apiUrl}/viagens`);
  });

  it('should reload the list when the dialog closes with a result (viagem created/updated)', async () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/viagens`).flush([]);
    await fixture.whenStable();
    fixture.detectChanges();

    (fixture.nativeElement.querySelector('button') as HTMLButtonElement).click();
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/veiculos`).flush([]);

    const dialog = TestBed.inject(MatDialog);
    dialog.openDialogs[0].close(VIAGEM_MOCK);
    // o fechamento do MatDialog aguarda a animação de saída (CSS) antes de completar
    // `afterClosed()`; jsdom não dispara essa animação, então esperamos o fallback
    // interno do Material (timeout) em vez de `fixture.whenStable()`.
    await new Promise((resolve) => setTimeout(resolve, 500));
    fixture.detectChanges();

    httpMock.expectOne(`${environment.apiUrl}/viagens`).flush([VIAGEM_MOCK]);
  });

  it('should delete the viagem and reload the list when the deletion is confirmed', async () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/viagens`).flush([VIAGEM_MOCK]);
    await fixture.whenStable();
    fixture.detectChanges();

    component.excluir(VIAGEM_MOCK);
    fixture.detectChanges();

    const dialog = TestBed.inject(MatDialog);
    dialog.openDialogs[0].close(true);
    await new Promise((resolve) => setTimeout(resolve, 500));
    fixture.detectChanges();

    httpMock.expectOne(`${environment.apiUrl}/viagens/${VIAGEM_MOCK.id}`).flush(null);
    await new Promise((resolve) => setTimeout(resolve, 0));
    await fixture.whenStable();

    httpMock.expectOne(`${environment.apiUrl}/viagens`).flush([]);
  });

  it('should not delete the viagem when the deletion is cancelled', async () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/viagens`).flush([VIAGEM_MOCK]);
    await fixture.whenStable();
    fixture.detectChanges();

    component.excluir(VIAGEM_MOCK);
    fixture.detectChanges();

    const dialog = TestBed.inject(MatDialog);
    dialog.openDialogs[0].close(false);
    await new Promise((resolve) => setTimeout(resolve, 500));
    fixture.detectChanges();

    httpMock.expectNone(`${environment.apiUrl}/viagens/${VIAGEM_MOCK.id}`);
  });
});
