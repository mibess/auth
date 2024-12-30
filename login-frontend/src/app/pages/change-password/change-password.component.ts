import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { UserSession } from '../../types/user';
import { MessageService } from '../../utils/message/message.service';
import { ChangePasswordRequest } from '../../types/change-password';
import { Router, RouterLink } from '@angular/router';
import { ButtonBackComponent } from "../../components/button-back/button-back.component";

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [FormsModule, RouterLink, ButtonBackComponent],
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.scss'
})
export class ChangePasswordComponent implements OnInit{

  authService = inject(AuthService);
  messageService = inject(MessageService);
  router = inject(Router);

  currentPassword: string = '';
  newPassword: string = '';
  confirmNewPassword: string = '';

  user: UserSession = {} as UserSession;

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.user = this.authService.getUserLogged();
    }
  }

  changePassword(): void {
    if (!this.validations()) return;

    const changePassword: ChangePasswordRequest = {
      email: this.user.email,
      oldPassword: this.currentPassword,
      newPassword: this.newPassword
    }

    this.authService.changePassword(changePassword).subscribe({
        next: () => {
          this.messageService.showMessage('Password changed successfully');
          this.router.navigate(['/']);
        },
        error: (e) => {
          this.messageService.showListMessage(e);
        }
      });

  }

  validations(): boolean {
    if (this.currentPassword === '' || this.newPassword === '' || this.confirmNewPassword === '') {
      this.messageService.showMessage('All fields are required');
      return false;
    }

    if (this.newPassword !== this.confirmNewPassword) {
      this.messageService.showMessage('Passwords do not match');
      return false;
    }

    return true;
  }

}
