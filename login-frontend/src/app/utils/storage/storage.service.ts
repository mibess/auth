import { Injectable } from '@angular/core';
import { StorageStrategy } from '../../types/storage-strategy';
import { LocalStorageStrategy } from './local-storage';
import { SessionStorageStrategy } from './session-storage';
import { CookieStorageStrategy } from './cookie-storage';

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  private strategy: StorageStrategy;

  constructor() {
    this.strategy = new LocalStorageStrategy(); // default strategy
  }

  setStrategy(strategy: StorageStrategy): void {
    this.strategy = strategy;
  }

  getItem(key: string): string | null {
    return this.strategy.getItem(key);
  }

  setItem(key: string, value: string): void {
    this.strategy.setItem(key, value);
  }

  removeItem(key: string): void {
    this.strategy.removeItem(key);
  }
}

export const STORAGE_TOKEN = 'la-token';
export const STORAGE_REFRESH_TOKEN = 'la-refreshToken';
