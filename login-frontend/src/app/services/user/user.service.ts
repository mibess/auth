import { UserUpdateRequest, UserResponse } from '../../types/user';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../auth/auth.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly apiBaseUrl: string = environment.API_BASE_URL + '/users';

  http = inject(HttpClient);
  authService = inject(AuthService);

  get(id: string) : Observable<UserResponse> {
    return this.http.get<UserResponse>(this.apiBaseUrl + '/' + id);
  }

  list() : Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(this.apiBaseUrl);
  }

  update(id: string, user: UserUpdateRequest) : Observable<UserResponse> {
    return this.http.put<UserResponse>(this.apiBaseUrl + '/' + id, user);
  }

  delete(id: string) : Observable<void> {
    return this.http.delete<void>(this.apiBaseUrl + '/' + id);
  }

}
