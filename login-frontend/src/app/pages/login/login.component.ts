import { SignalStorageService } from './../../utils/signals/signal-storage.service';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { MessageService } from '../../utils/message/message.service';
import { StorageService } from '../../utils/storage/storage.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ FormsModule, RouterLink ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  authService = inject(AuthService);
  messageService = inject(MessageService);
  signalStorageService = inject(SignalStorageService);
  storageService = inject(StorageService);

  router = inject(Router);

  email: string = '';
  password: string = '';

  login() {
    if (!this.validations()) return;

    this.authService.login(this.email, this.password).subscribe({
      next: (response)=> {
        if (response.accessToken){

          this.authService.setStorageToken(response.accessToken, response.refreshToken);

          const user = this.authService.getUserLogged();

          this.signalStorageService.setUser(user);

          this.router.navigateByUrl('/');
        }
      },
      error: (e)=> {
        this.messageService.showMessage(e.error.message);
      }
    });
  }

  loginWithGoogle() {

    this.authService.loginWithGoogle();

  }

  private validations() {
    if (this.email === '') {
      this.messageService.showMessage('Email is required');
      return false;
    }

    if (this.password === '') {
      this.messageService.showMessage('Password is required');
      return false;
    }
    return true;
  }

}
