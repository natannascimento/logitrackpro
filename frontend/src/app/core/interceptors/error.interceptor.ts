import { inject } from '@angular/core';
import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

import { AuthService } from '../auth/auth-service';

interface ApiError {
  message?: string;
}

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const snackBar = inject(MatSnackBar);
  const authService = inject(AuthService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: unknown) => {
      if (error instanceof HttpErrorResponse) {
        const isLoginRequest = req.url.endsWith('/auth/login');

        if (error.status === 401 && !isLoginRequest) {
          // 401 em endpoint protegido indica sessão expirada/token inválido.
          authService.logout();
          router.navigateByUrl('/login');
        } else if (error.status !== 400 && !isLoginRequest) {
          // 400 é tratado inline pelo componente que fez a requisição (ex.: dialog
          // de viagem exibe o erro sem fechar); 401 em /auth/login é erro de
          // credenciais, tratado inline pelo próprio formulário de login; aqui só
          // mostramos feedback genérico para erros que nenhuma tela trata
          // especificamente (404, 5xx, rede).
          const apiError = error.error as ApiError | null;
          const message = apiError?.message ?? 'Ocorreu um erro ao comunicar com o servidor. Tente novamente.';
          snackBar.open(message, 'Fechar', { duration: 5000 });
        }
      }

      return throwError(() => error);
    }),
  );
};
