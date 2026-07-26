import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

import { Viagem } from '../../../../shared/models/viagem.model';

export interface ConfirmarExclusaoDialogData {
  viagem: Viagem;
}

@Component({
  selector: 'app-confirmar-exclusao-dialog',
  imports: [MatDialogModule, MatButtonModule],
  templateUrl: './confirmar-exclusao-dialog.html',
  styleUrl: './confirmar-exclusao-dialog.scss',
})
export class ConfirmarExclusaoDialog {
  private readonly dialogRef = inject(MatDialogRef<ConfirmarExclusaoDialog>);
  protected readonly data = inject<ConfirmarExclusaoDialogData>(MAT_DIALOG_DATA);

  confirmar(): void {
    this.dialogRef.close(true);
  }

  cancelar(): void {
    this.dialogRef.close(false);
  }
}
