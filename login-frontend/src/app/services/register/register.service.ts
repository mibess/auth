import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { UserRegister, UserResponse } from '../../types/user';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  private readonly apiBaseUrl: string = environment.API_BASE_URL + '/users';

  http = inject(HttpClient);
  authService = inject(AuthService);

  register(user : UserRegister) : Observable<UserResponse> {
    return this.http.post<UserResponse>(this.apiBaseUrl, user);
  }

  registerWithGoogle() {
    this.authService.loginWithGoogle();
  }

}
