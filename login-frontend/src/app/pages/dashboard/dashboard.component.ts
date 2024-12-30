import { UserResponse } from './../../types/user';
import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { UserSession } from '../../types/user';
import { UserService } from '../../services/user/user.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {
  authService = inject(AuthService);
  userService = inject(UserService);

  router = inject(Router);
  route = inject(ActivatedRoute);

  logged = false;

  user: UserSession = {} as UserSession;

  userEntity: UserResponse = {} as UserResponse;

  ngOnInit() {
    this.logged = this.authService.isAuthenticated();
    this.user = this.authService.getUserLogged();
    this.findUserEntity();
  }

  private findUserEntity(): void {
    this.userService.get(this.user.id).subscribe({
      next: (response) => {
        this.userEntity = response;
      },
      error: (error) => {
        console.error('Error: ', error);
      }
    });
  }

}
