import { UserRegister, UserResponse } from './../../types/user';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RegisterService } from '../../services/register/register.service';
import { Router, RouterLink } from '@angular/router';
import { MessageService } from '../../utils/message/message.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ FormsModule, RouterLink ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  registerService = inject(RegisterService);
  router = inject(Router);
  message = inject(MessageService);

  user: UserRegister = {} as UserRegister;
  userResponse: UserResponse = {} as UserResponse;

  confirmPassword: string = '';

  register() {
    if (!this.validatePassword()) return;

    this.user.role = 'USER';

    this.registerService.register(
      this.user
    ).subscribe({
      next: (response) => {
        this.userResponse = response;

        if (this.userResponse.id) {
          this.message.showMessage('User registered successfully, please login');
          this.router.navigateByUrl('/login');
        }
      },
      error: (e) => {
        console.log(e.error);
        this.message.showMessage(e.error.message);
      }
    });
  }

  registerWithGoogle() {
    this.registerService.registerWithGoogle();
  }

  validatePassword(): boolean {
    const valid = this.user.password === this.confirmPassword;

    if (!valid) {
      this.message.showMessage('Password and Confirm Password must be the same');
      return false;
    }
    return true;
  }

}
