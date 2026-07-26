import { vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { ViagemFormDialog } from './viagem-form-dialog';
import { environment } from '../../../../../environments/environment';

describe('ViagemFormDialog', () => {
  let component: ViagemFormDialog;
  let fixture: ComponentFixture<ViagemFormDialog>;
  let dialogRefSpy: { close: ReturnType<typeof vi.fn> };

  beforeEach(async () => {
    dialogRefSpy = { close: vi.fn() };

    await TestBed.configureTestingModule({
      imports: [ViagemFormDialog],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: MatDialogRef, useValue: dialogRefSpy },
        { provide: MAT_DIALOG_DATA, useValue: {} },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ViagemFormDialog);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should be invalid when kmPercorrida is zero or negative', () => {
    fixture.detectChanges();
    component['form'].patchValue({
      veiculoId: 1,
      dataSaida: '2024-05-01T08:00',
      kmPercorrida: 0,
    });
    expect(component['form'].get('kmPercorrida')?.valid).toBe(false);
  });

  it('should be invalid when dataChegada is before dataSaida', () => {
    fixture.detectChanges();
    component['form'].patchValue({
      veiculoId: 1,
      dataSaida: '2024-05-01T18:00',
      dataChegada: '2024-05-01T08:00',
      kmPercorrida: 10,
    });
    expect(component['form'].errors?.['dataChegadaAntesDaSaida']).toBe(true);
  });

  it('should be valid and close the dialog with the created viagem on submit', async () => {
    fixture.detectChanges();
    component['form'].patchValue({
      veiculoId: 1,
      dataSaida: '2024-05-01T08:00',
      kmPercorrida: 100,
    });
    expect(component['form'].valid).toBe(true);

    const httpMock = TestBed.inject(HttpTestingController);
    const salvarPromise = component.salvar();

    const req = httpMock.expectOne(`${environment.apiUrl}/viagens`);
    expect(req.request.method).toBe('POST');
    req.flush({ id: 1 });
    await salvarPromise;

    expect(dialogRefSpy.close).toHaveBeenCalledWith({ id: 1 });
  });
});
