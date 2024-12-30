import { LogoutRequest } from '../../types/logout';
import { TokenResponse } from '../../types/token';
import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserSession } from '../../types/user';
import { API_ENDPOINTS } from '../../core/endpoints/endpoints';
import { StorageService } from '../../utils/storage/storage.service';
import { TokenService } from '../../utils/token/token.service';
import { ChangePasswordRequest } from '../../types/change-password';
import { ForgotPasswordRequest } from '../../types/forgot-password';
import { ResetPasswordRequest } from '../../types/reset-password';
import { Router } from '@angular/router';
import { MessageService } from '../../utils/message/message.service';
import { CreatePasswordRequest } from '../../types/create-password';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  storageService = inject(StorageService);
  tokenService = inject(TokenService);
  message = inject(MessageService);

  http = inject(HttpClient);
  route = inject(Router);

  private readonly userLogged: UserSession = {} as UserSession;

  login(email: string, password: string) : Observable<TokenResponse> {
    return this.http.post<TokenResponse>(API_ENDPOINTS.LOGIN, {
      email: email,
      password: password
    });
  }

  loginWithGoogle() {
    window.location.href = API_ENDPOINTS.GOOGLE;
  }

  changePassword(changePassword: ChangePasswordRequest): Observable<void> {
    return this.http.post<void>(API_ENDPOINTS.CHANGE_PASSWORD, changePassword);
  }

  forgotPassword(forgotPassword: ForgotPasswordRequest): Observable<void> {
    return this.http.post<void>(API_ENDPOINTS.FORGOT_PASSWORD, forgotPassword);
  }

  validateResetPasswordToken(token: string): Observable<void> {
    return this.http.get<void>(API_ENDPOINTS.VALIDATE_RESET_PASSWORD_TOKEN + '/' + token);
  }

  resetPassword(resetPasswordRequest: ResetPasswordRequest): Observable<void> {
    return this.http.post<void>(API_ENDPOINTS.RESET_PASSWORD, resetPasswordRequest);
  }

  createPassword(createPassword: CreatePasswordRequest): Observable<void> {
    return this.http.post<void>(API_ENDPOINTS.CREATE_PASSWORD, createPassword);
  }

  emailValidation(token: string): Observable<void> {
    return this.http.get<void>(API_ENDPOINTS.VALIDATE_EMAIL + '/' + token);
  }

  getAccessToken(code: string) : Observable<TokenResponse> {
    return this.http.post<TokenResponse>(API_ENDPOINTS.GOOGLE_CALLBACK, {
      code: code
    });
  }

  createRefreshToken(refreshToken: string | null) : Observable<TokenResponse> {
    return this.http.post<TokenResponse>(API_ENDPOINTS.REFRESH_TOKEN, {
      refreshToken: refreshToken
    });
  }

  logout(logoutRequest: LogoutRequest): Observable<void> {
    return this.http.post<void>(API_ENDPOINTS.LOGOUT, {
      refreshToken: logoutRequest.refreshToken
    });
  }

  getUserLogged(): UserSession {
    if (!this.isAuthenticated()) return this.userLogged;

    const token = this.tokenService.getToken();

    if (!token) return this.userLogged;

    const payload = this.tokenService.decodePayload(token);

    const user: UserSession = {
      id: payload.sub,
      name: payload.name,
      lastName: payload.lastName,
      email: payload.email,
      role: payload.realm_access.roles[0]
    }
    return user;
  }

  cleanUserSession(): void {
    this.tokenService.cleanTokens();
  }

  getAuthHeaders(): HttpHeaders {
    const token = this.tokenService.getToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }


  isAuthenticated(): boolean {
    return this.tokenService.isAuthenticated();
  }

  setStorageToken(token: string, refreshToken: string): void {

    this.tokenService.setToken(token);
    this.tokenService.setRefreshToken(refreshToken);
    this.autoRefreshToken();

  }

  autoRefreshToken(): void {
    const token = this.tokenService.getToken();

    if (!token) return;

    const payload = this.tokenService.decodePayload(token);
    const expiration = payload.exp * 1000;
    const timeout = expiration - Date.now() - (60 * 1000);

    this.scheduleRefreshToken(timeout);
  }

  scheduleRefreshToken(timeout: number): void {
    setTimeout(() => {
      this.createRefreshToken(this.tokenService.getRefreshToken()).subscribe({
        next: (response) => {
          if (response.accessToken){
            this.setStorageToken(response.accessToken, response.refreshToken);
          }
        },
        error: (e)=> {
          console.error('Refresh token failed', e.error.message);
        }
      });
    }, timeout);
  }


}
