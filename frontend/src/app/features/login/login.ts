import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

import { AuthService } from '../../core/auth/auth-service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  protected entrando = false;
  protected readonly erroApi = signal<string | null>(null);

  protected readonly form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    senha: ['', Validators.required],
  });

  async entrar(): Promise<void> {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { email, senha } = this.form.getRawValue();

    this.entrando = true;
    this.erroApi.set(null);
    try {
      await this.authService.login({ email: email!, senha: senha! });
      this.router.navigateByUrl('/viagens');
    } catch (error) {
      if (error instanceof HttpErrorResponse && error.status === 401) {
        this.erroApi.set('E-mail ou senha inválidos.');
      } else {
        throw error;
      }
    } finally {
      this.entrando = false;
    }
  }
}
