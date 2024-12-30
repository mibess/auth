import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-email-validation',
  standalone: true,
  imports: [ FormsModule ],
  templateUrl: './email-validation.component.html',
  styleUrl: './email-validation.component.scss'
})
export class EmailValidationComponent  implements OnInit {
  authService = inject(AuthService);

  router = inject(Router);
  route = inject(ActivatedRoute);

  ngOnInit(): void {
    this.validToken();
  }

  getToken(): string | null {
    return this.route.snapshot.paramMap.get('token');
  }

  private validToken(): void {
    const token = this.getToken();
    if (!token) {
      this.router.navigate(['/']);
      return;
    }

    setTimeout(() => {
      this.authService.emailValidation(token ?? '').subscribe(() => {
        this.router.navigate(['/']);
      });
    }, 5000);

  }

}
