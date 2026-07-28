import { Component, computed, inject, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';

import { ViagemService } from './services/viagem.service';
import { Viagem } from '../../shared/models/viagem.model';
import { ViagemFormDialog, ViagemFormDialogData } from './components/viagem-form-dialog/viagem-form-dialog';
import { ConfirmarExclusaoDialog } from './components/confirmar-exclusao-dialog/confirmar-exclusao-dialog';

@Component({
  selector: 'app-viagens',
  imports: [MatTableModule, MatButtonModule, MatIconModule, DatePipe],
  templateUrl: './viagens.html',
  styleUrl: './viagens.scss',
})
export class Viagens implements OnInit {
  private readonly viagemService = inject(ViagemService);
  private readonly dialog = inject(MatDialog);

  protected readonly colunas = [
    'veiculo',
    'origem',
    'destino',
    'dataSaida',
    'dataChegada',
    'kmPercorrida',
    'acoes',
  ];
  protected readonly viagens = computed(() => this.viagemService.viagens());

  ngOnInit(): void {
    this.viagemService.carregarViagens();
  }

  novaViagem(): void {
    this.abrirDialog();
  }

  editar(viagem: Viagem): void {
    this.abrirDialog({ viagem });
  }

  excluir(viagem: Viagem): void {
    const dialogRef = this.dialog.open(ConfirmarExclusaoDialog, { data: { viagem } });
    dialogRef.afterClosed().subscribe(async (confirmado) => {
      if (confirmado) {
        await this.viagemService.excluir(viagem.id);
        this.viagemService.carregarViagens();
      }
    });
  }

  private abrirDialog(data?: ViagemFormDialogData): void {
    const dialogRef = this.dialog.open(ViagemFormDialog, { data, width: 'min(560px, 95vw)' });
    dialogRef.afterClosed().subscribe((resultado) => {
      if (resultado) {
        this.viagemService.carregarViagens();
      }
    });
  }
}
