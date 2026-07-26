import { inject } from '@angular/core';
import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, throwError } from 'rxjs';

interface ApiError {
  message?: string;
}

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const snackBar = inject(MatSnackBar);

  return next(req).pipe(
    catchError((error: unknown) => {
      if (error instanceof HttpErrorResponse) {
        // 400 é tratado inline pelo componente que fez a requisição (ex.: dialog
        // de viagem exibe o erro sem fechar); aqui só mostramos feedback genérico
        // para erros que nenhuma tela trata especificamente (404, 5xx, rede).
        if (error.status !== 400) {
          const apiError = error.error as ApiError | null;
          const message = apiError?.message ?? 'Ocorreu um erro ao comunicar com o servidor. Tente novamente.';
          snackBar.open(message, 'Fechar', { duration: 5000 });
        }
      }

      return throwError(() => error);
    }),
  );
};
