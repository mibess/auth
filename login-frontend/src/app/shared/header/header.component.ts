import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { Router } from '@angular/router';
import { SignalStorageService } from '../../utils/signals/signal-storage.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  authService = inject(AuthService);
  signalStorageService = inject(SignalStorageService);

  route = inject(Router);
  user = this.signalStorageService.user;

  constructor() {

    if (!this.user().id) {
      const userSession = this.authService.getUserLogged();
      if (userSession?.id) this.signalStorageService.setUser(userSession);
    }

  }

  logout() {
    this.route.navigate(['/logout']);
  }

}
