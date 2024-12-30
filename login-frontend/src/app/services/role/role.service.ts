import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../auth/auth.service';
import { Observable } from 'rxjs';
import { RoleResponse } from '../../types/role';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  private readonly apiBaseUrl: string = environment.API_BASE_URL + '/roles';

  http = inject(HttpClient);
  authService = inject(AuthService);

  getAdmin() : Observable<RoleResponse> {
    return this.http.get<RoleResponse>(this.apiBaseUrl + '/admin', {
      headers: this.authService.getAuthHeaders()
    });
  }


  getUser() : Observable<RoleResponse> {
    return this.http.get<RoleResponse>(this.apiBaseUrl + '/user', {
      headers: this.authService.getAuthHeaders()
    });
  }

}
