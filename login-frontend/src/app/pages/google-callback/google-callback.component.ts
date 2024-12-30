import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-google-callback',
  standalone: true,
  imports: [],
  templateUrl: './google-callback.component.html'
})
export class GoogleCallbackComponent {
  authService = inject(AuthService);

  router = inject(Router);

  ngOnInit(): void {
    const params = new URLSearchParams(window.location.search);
    const code = params.get('code');

    if (code) {

      this.authService.getAccessToken(code).subscribe((response) => {
        this.authService.setStorageToken(response.accessToken, response.refreshToken);
        this.router.navigate(['/']);
      });

    }

  }
}
