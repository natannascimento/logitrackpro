import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

import { environment } from '../../../environments/environment';
import { LoginRequest, LoginResponse } from '../../shared/models/auth.model';

const TOKEN_STORAGE_KEY = 'logitrackpro.accessToken';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/auth`;

  private readonly token = signal<string | null>(localStorage.getItem(TOKEN_STORAGE_KEY));

  readonly isAuthenticated = computed(() => this.token() !== null);

  async login(request: LoginRequest): Promise<void> {
    const response = await firstValueFrom(this.http.post<LoginResponse>(`${this.apiUrl}/login`, request));
    localStorage.setItem(TOKEN_STORAGE_KEY, response.accessToken);
    this.token.set(response.accessToken);
  }

  logout(): void {
    localStorage.removeItem(TOKEN_STORAGE_KEY);
    this.token.set(null);
  }

  getToken(): string | null {
    return this.token();
  }
}
