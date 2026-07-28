import { Component, inject, OnInit, signal } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroupDirective,
  NgForm,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { ErrorStateMatcher } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

import { ViagemService } from '../../services/viagem.service';
import { Viagem } from '../../../../shared/models/viagem.model';

export interface ViagemFormDialogData {
  viagem?: Viagem;
}

function kmPercorridaPositiva(control: AbstractControl): ValidationErrors | null {
  const valor = control.value;
  return valor !== null && valor !== '' && Number(valor) > 0 ? null : { kmInvalida: true };
}

function dataChegadaAposSaida(control: AbstractControl): ValidationErrors | null {
  const dataSaida = control.get('dataSaida')?.value;
  const dataChegada = control.get('dataChegada')?.value;
  if (!dataSaida || !dataChegada) {
    return null;
  }
  return new Date(dataChegada) >= new Date(dataSaida) ? null : { dataChegadaAntesDaSaida: true };
}

class DataChegadaErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const parent = control?.parent;
    return !!(parent?.hasError('dataChegadaAntesDaSaida') && (control?.touched || form?.submitted));
  }
}

@Component({
  selector: 'app-viagem-form-dialog',
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
  ],
  templateUrl: './viagem-form-dialog.html',
  styleUrl: './viagem-form-dialog.scss',
})
export class ViagemFormDialog implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly viagemService = inject(ViagemService);
  private readonly dialogRef = inject(MatDialogRef<ViagemFormDialog>);
  protected readonly data = inject<ViagemFormDialogData>(MAT_DIALOG_DATA, { optional: true }) ?? {};

  protected readonly veiculos = this.viagemService.veiculos;
  protected salvando = false;
  protected readonly erroApi = signal<string | null>(null);
  protected readonly matcherDataChegada = new DataChegadaErrorStateMatcher();

  protected readonly form = this.fb.group(
    {
      veiculoId: [this.data.viagem?.veiculo.id ?? null, Validators.required],
      dataSaida: [this.data.viagem?.dataSaida.slice(0, 16) ?? '', Validators.required],
      dataChegada: [this.data.viagem?.dataChegada?.slice(0, 16) ?? ''],
      origem: [this.data.viagem?.origem ?? ''],
      destino: [this.data.viagem?.destino ?? ''],
      kmPercorrida: [this.data.viagem?.kmPercorrida ?? null, [Validators.required, kmPercorridaPositiva]],
    },
    { validators: dataChegadaAposSaida },
  );

  ngOnInit(): void {
    this.viagemService.carregarVeiculos();
  }

  async salvar(): Promise<void> {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const valores = this.form.getRawValue();
    const request = {
      veiculoId: valores.veiculoId!,
      dataSaida: valores.dataSaida!,
      dataChegada: valores.dataChegada || null,
      origem: valores.origem || null,
      destino: valores.destino || null,
      kmPercorrida: Number(valores.kmPercorrida),
    };

    this.salvando = true;
    this.erroApi.set(null);
    try {
      const viagem = this.data.viagem
        ? await this.viagemService.atualizar(this.data.viagem.id, request)
        : await this.viagemService.criar(request);
      this.dialogRef.close(viagem);
    } catch (error) {
      if (error instanceof HttpErrorResponse && error.status === 400) {
        const apiError = error.error as { message?: string; details?: string[] };
        this.erroApi.set(apiError.details?.length ? apiError.details.join('; ') : (apiError.message ?? 'Dados inválidos'));
      } else {
        throw error;
      }
    } finally {
      this.salvando = false;
    }
  }

  cancelar(): void {
    this.dialogRef.close();
  }
}
