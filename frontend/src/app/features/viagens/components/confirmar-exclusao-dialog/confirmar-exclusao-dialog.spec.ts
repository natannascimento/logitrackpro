import { vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { ConfirmarExclusaoDialog } from './confirmar-exclusao-dialog';
import { Viagem } from '../../../../shared/models/viagem.model';

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

describe('ConfirmarExclusaoDialog', () => {
  let component: ConfirmarExclusaoDialog;
  let fixture: ComponentFixture<ConfirmarExclusaoDialog>;
  let dialogRefSpy: { close: ReturnType<typeof vi.fn> };

  beforeEach(async () => {
    dialogRefSpy = { close: vi.fn() };

    await TestBed.configureTestingModule({
      imports: [ConfirmarExclusaoDialog],
      providers: [
        { provide: MatDialogRef, useValue: dialogRefSpy },
        { provide: MAT_DIALOG_DATA, useValue: { viagem: VIAGEM_MOCK } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ConfirmarExclusaoDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should close with true when confirmar() is called', () => {
    component.confirmar();
    expect(dialogRefSpy.close).toHaveBeenCalledWith(true);
  });

  it('should close with false when cancelar() is called', () => {
    component.cancelar();
    expect(dialogRefSpy.close).toHaveBeenCalledWith(false);
  });
});
