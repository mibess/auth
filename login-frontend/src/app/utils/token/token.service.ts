import { inject, Injectable } from '@angular/core';
import { STORAGE_REFRESH_TOKEN, StorageService, STORAGE_TOKEN } from '../storage/storage.service';
import { LocalStorageStrategy } from '../storage/local-storage';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  storageService = inject(StorageService);

  constructor() {
    this.storageService.setStrategy(new LocalStorageStrategy());
  }

  getToken(): string | null {
    return this.storageService.getItem(STORAGE_TOKEN);
  }

  setToken(token: string) {
    this.storageService.setItem(STORAGE_TOKEN, token);
  }

  getRefreshToken(): string | null {
    return this.storageService.getItem(STORAGE_REFRESH_TOKEN);
  }

  setRefreshToken(refreshToken: string) {
    this.storageService.setItem(STORAGE_REFRESH_TOKEN, refreshToken);
  }

  removeToken() {
    this.storageService.removeItem(STORAGE_TOKEN);
  }

  removeRefreshToken() {
    this.storageService.removeItem(STORAGE_REFRESH_TOKEN);
  }

  cleanTokens() {
    this.removeToken();
    this.removeRefreshToken();
  }

  isAuthenticated(): boolean {
    const token = this.getToken();

    if (!token) return false;

    return this.isExpired(token);
  }

  decodePayload(token: string): any {
    if (!token || token.split('.').length !== 3) {
      throw new Error('Invalid token format');
    }
    return jwtDecode(token);
  }

  private getExpiration(token: string): number {
    const payload = this.decodePayload(token);
    return payload.exp * 1000;
  }

  private isExpired(token: string): boolean {
    const expiration = this.getExpiration(token);
    return Date.now() < expiration;
  }

}
