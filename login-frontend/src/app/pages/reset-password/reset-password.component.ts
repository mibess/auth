import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { FormsModule } from '@angular/forms';
import { ResetPasswordRequest } from '../../types/reset-password';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MessageService } from '../../utils/message/message.service';
import { ButtonBackComponent } from "../../components/button-back/button-back.component";

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [FormsModule, RouterLink, ButtonBackComponent],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.scss'
})
export class ResetPasswordComponent implements OnInit {

  authService = inject(AuthService);
  messageService = inject(MessageService);

  router = inject(Router);
  route = inject(ActivatedRoute);

  newPassword: string = '';
  confirmPassword: string = '';

  ngOnInit(): void {
    this.validToken();
  }

  sendResetPassword() : void {
    this.validToken();

    if (!this.validation()) {
      this.messageService.showMessage('Passwords do not match');
      return;
    }

    const token = this.getToken();

    const resetPasswordRequest: ResetPasswordRequest = {
      token: token ?? '',
      newPassword: this.newPassword
    };

    this.resetPassword(resetPasswordRequest);

  }

  getToken(): string | null {
    return this.route.snapshot.paramMap.get('token');
  }

  validation(): boolean {
    return this.newPassword === this.confirmPassword;
  }

  private resetPassword(resetPassword: ResetPasswordRequest): void {
    this.authService.resetPassword(resetPassword).subscribe({
      next: () => {
        this.messageService.showMessage('Password reset successfully');
        this.router.navigate(['/login']);
      },
      error: (e) => {
        this.messageService.showMessage(e.error.message);
      }
    });
  }

  private validToken(): void {
    const token = this.getToken();
    if (!token) {
      this.messageService.showMessage('Invalid token');
      this.router.navigate(['/login']);
      return;
    }

    this.authService.validateResetPasswordToken(token ?? '').subscribe({
      next: () => {},
      error: (e) => {
        this.messageService.showMessage(e.error.message);
        this.router.navigate(['/login']);
      }
    });
  }
}
