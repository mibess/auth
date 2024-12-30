import { UserSession } from './../../types/user';
import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { RoleService } from '../../services/role/role.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-role',
  standalone: true,
  imports: [ RouterLink ],
  templateUrl: './role.component.html',
  styleUrl: './role.component.scss'
})
export class RoleComponent {
  authService = inject(AuthService);
  roleService = inject(RoleService);

  user: UserSession = {} as UserSession;

  resources: any[] = [];

  ngOnInit() {
    this.user = this.authService.getUserLogged();

    this.getUserResources();
    this.getAdminResources();

  }

  private getAdminResources(): void {
    this.roleService.getAdmin().subscribe({
      next: (data) => {
        this.resources.push(data.response);
      },
      error: (error) => {
        this.resources.push('Admin Resources - ' + this.messageError(error));
      }
    });
  }

  private getUserResources(): void {
    this.roleService.getUser().subscribe({
      next: (data) => {
        this.resources.push(data.response);
      },
      error: (error) => {
        this.resources.push('User Resources - ' + this.messageError(error));
      }
    });
  }

  messageError(error: any): string {
    if (error.status === 401 || error.status === 403) {
      return `You can't to access this resource`;
      } else {
        return `Error: ${error.status} - ${error.message}`;
      }
  }
}
