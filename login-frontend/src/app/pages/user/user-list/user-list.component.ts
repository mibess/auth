import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { UserService } from '../../../services/user/user.service';
import { UserResponse } from '../../../types/user';
import { MessageService } from '../../../utils/message/message.service';
import { AuthService } from '../../../services/auth/auth.service';
import { ButtonBackComponent } from "../../../components/button-back/button-back.component";

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [RouterLink, ButtonBackComponent],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.scss'
})
export class UserListComponent {
  authService = inject(AuthService);
  userService = inject(UserService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  messageService = inject(MessageService);

  users = [] as UserResponse[];
  isLoading = true;

  ngOnInit(): void {
    this.getUsers();
  }

  deleteUser(id: string): void {
    if (!confirm('Are you sure you want to delete this user?')){
      return;
    }

    const userLogged = this.authService.getUserLogged();

    if (userLogged.id === id) {
      this.messageService.showMessage('You cannot delete yourself');
      return;
    }

    this.userService.delete(id).subscribe({
      next: () => {
        this.messageService.showMessage('User deleted');
        this.getUsers();
      },
      error: (e) => {
        this.messageService.showListMessage(e);
      }
    });
  }

  private getUsers(): void {
    this.userService.list().subscribe({
      next: (response) => {
        this.users = response;
      },
      error: (e) => {
        console.error('Users not found', e);
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }
}
