import { StorageStrategy } from "../../types/storage-strategy";

export class CookieStorageStrategy implements StorageStrategy {

  getItem(key: string): string | null {
    const cookies = document.cookie.split('; ').reduce((acc, cookie) => {
      const [k, v] = cookie.split('=');
      acc[k] = v;
      return acc;
    }, {} as Record<string, string>);
    return cookies[key] || null;
  }

  setItem(key: string, value: string): void {
    document.cookie = `${key}=${value}; path=/;`;
  }

  removeItem(key: string): void {
    document.cookie = `${key}=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT;`;
  }

}
