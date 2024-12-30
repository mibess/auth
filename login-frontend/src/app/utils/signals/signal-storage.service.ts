import { computed, Injectable, signal } from '@angular/core';
import { UserSession } from '../../types/user';

@Injectable({
  providedIn: 'root'
})
export class SignalStorageService {
  private readonly userSignal = signal<UserSession>({} as UserSession);

  public readonly user = computed(() => { return this.userSignal() });

  public setUser(user: UserSession): void {
    this.userSignal.set(user);
  }

  public getUser(): UserSession {
    return this.userSignal();
  }
}
