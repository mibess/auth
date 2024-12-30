import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { ForgotPasswordRequest } from '../../types/forgot-password';
import { MessageService } from '../../utils/message/message.service';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [ FormsModule, RouterLink ],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.scss'
})
export class ForgotPasswordComponent {
  authService = inject(AuthService);
  messageService = inject(MessageService);

  forgotPassword: ForgotPasswordRequest = {
    email: ''
  };

  sendForgotPassword() : void {
    if (!this.forgotPassword.email){
      this.messageService.showMessage('Email is required');
      return;
    }

    this.authService.forgotPassword(this.forgotPassword).subscribe({
      next: () => {
        this.messageService.showMessage('Email sent to: ' + this.forgotPassword.email + " successfully!");
        this.forgotPassword.email = '';
      },
      error: (e) => {
        this.messageService.showListMessage(e);
      }
    });
  }
}
