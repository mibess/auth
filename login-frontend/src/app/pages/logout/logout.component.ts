import { SignalStorageService } from './../../utils/signals/signal-storage.service';
import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { Router } from '@angular/router';
import { TokenService } from '../../utils/token/token.service';
import { LogoutRequest } from '../../types/logout';

@Component({
  selector: 'app-logout',
  standalone: true,
  imports: [],
  templateUrl: './logout.component.html',
  styleUrl: './logout.component.scss'
})
export class LogoutComponent implements OnInit {

  authService = inject(AuthService);
  tokenService = inject(TokenService);
  signalStorageService = inject(SignalStorageService);

  route = inject(Router);

  ngOnInit(): void {
    this.logout();
  }

  private logout() : void {

    setTimeout(() => {
      const refreshToken = this.tokenService.getRefreshToken();

      if (!refreshToken) return;

      const logoutRequest: LogoutRequest = { refreshToken: refreshToken };

      this.authService.logout( logoutRequest ).subscribe({
        next: () => {
            this.authService.cleanUserSession();
            this.route.navigate(['/login']);
          },
          error: (e) => {
            console.log(e);
          }
      });

    }, 5000);
  }

}
