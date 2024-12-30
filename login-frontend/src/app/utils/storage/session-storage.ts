import { StorageStrategy } from "../../types/storage-strategy";

export class SessionStorageStrategy implements StorageStrategy {

  getItem(key: string): string | null {
    return sessionStorage.getItem(key);
  }

  setItem(key: string, value: string): void {
    sessionStorage.setItem(key, value);
  }

  removeItem(key: string): void {
    sessionStorage.removeItem(key);
  }

}
