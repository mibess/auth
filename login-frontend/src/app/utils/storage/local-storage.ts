import { StorageStrategy } from "../../types/storage-strategy";

export class LocalStorageStrategy implements StorageStrategy {

  getItem(key: string): string | null {
    if (typeof window === 'undefined') return null;

    return localStorage.getItem(key);
  }

  setItem(key: string, value: string): void {
    localStorage.setItem(key, value);
  }

  removeItem(key: string): void {
    localStorage.removeItem(key);
  }

}
