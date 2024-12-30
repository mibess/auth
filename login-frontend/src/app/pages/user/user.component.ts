import { UserService } from '../../services/user/user.service';
import { Component, inject, OnInit } from '@angular/core';
import { UserUpdateRequest } from '../../types/user';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MessageService } from '../../utils/message/message.service';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [ FormsModule, RouterLink ],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss'
})
export class UserComponent implements OnInit {
  userService = inject(UserService);
  message = inject(MessageService);

  route = inject(ActivatedRoute);
  router = inject(Router);

  user: UserUpdateRequest = {} as UserUpdateRequest;
  isLoading = true;

  ngOnInit(): void {
    const userId = this.getUserId();

    if (!userId) {
      this.isLoading = false;
      return;
    }

    this.userService.get(userId).subscribe({
      next: (response) => {
        this.user = response;
      },
      error: (e) => {
        this.message.showMessage(e.error.message);
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  update(): void {
    const userId = this.getUserId();

    if (!userId) {
      return;
    }

    this.userService.update(userId, this.user).subscribe({
      next: () => {
        this.message.showMessage('User updated');
        this.router.navigateByUrl('/users');
      },
      error: (e) => {
        this.message.showMessage(e.error.message);
      }
    });
  }

  private getUserId(): string | null {
    const userId = this.route.snapshot.paramMap.get('id');

    if (!userId) {
      return null;
    }

    return userId;
  }

}
