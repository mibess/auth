import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { UserSession } from '../../types/user';
import { MessageService } from '../../utils/message/message.service';
import { Router } from '@angular/router';
import { CreatePasswordRequest } from '../../types/create-password';
import { ButtonBackComponent } from "../../components/button-back/button-back.component";

@Component({
  selector: 'app-create-password',
  standalone: true,
  imports: [FormsModule, ButtonBackComponent],
  templateUrl: './create-password.component.html',
  styleUrl: './create-password.component.scss'
})
export class CreatePasswordComponent implements OnInit{

  authService = inject(AuthService);
  messageService = inject(MessageService);
  router = inject(Router);

  newPassword: string = '';
  confirmNewPassword: string = '';

  user: UserSession = {} as UserSession;

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.user = this.authService.getUserLogged();
    }
  }

  createPassword(): void {
    if (!this.validations()) return;

    const createPassword: CreatePasswordRequest = {
      email: this.user.email,
      password: this.newPassword
    }

    this.authService.createPassword(createPassword).subscribe({
        next: () => {
          this.messageService.showMessage('Password create successfully');
          this.router.navigate(['/']);
        },
        error: (e) => {
          this.messageService.showListMessage(e);
        }
      });

  }

  validations(): boolean {
    if (this.newPassword === '' || this.confirmNewPassword === '') {
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
