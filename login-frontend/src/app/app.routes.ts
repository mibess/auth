import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { RegisterComponent } from './pages/register/register.component';
import { GoogleCallbackComponent } from './pages/google-callback/google-callback.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { AuthGuard } from './core/guards/auth.guard';
import { AuthenticatedGuard } from './core/guards/authenticated.guard';
import { UserComponent } from './pages/user/user.component';
import { UserListComponent } from './pages/user/user-list/user-list.component';
import { RoleComponent } from './pages/role/role.component';
import { ChangePasswordComponent } from './pages/change-password/change-password.component';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './pages/reset-password/reset-password.component';
import { LayoutComponent } from './shared/layout/layout.component';
import { LogoutComponent } from './pages/logout/logout.component';
import { CreatePasswordComponent } from './pages/create-password/create-password.component';
import { EmailValidationComponent } from './pages/email-validation/email-validation.component';

export const routes: Routes = [
  { path: '', component: LayoutComponent,  children: [
    //{ path: '', redirectTo: '', pathMatch: 'full' },
    { path: '', component: DashboardComponent },

    { path: 'users', component: UserListComponent },
    { path: 'users/:id', component: UserComponent },
    { path: 'change-password', component: ChangePasswordComponent },
    { path: 'create-password', component: CreatePasswordComponent },
    { path: 'roles', component: RoleComponent },
  ], canActivate: [AuthGuard] },

  { path: 'login', component: LoginComponent, canActivate: [AuthenticatedGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [AuthenticatedGuard] },
  { path: 'forgot-password', component: ForgotPasswordComponent, canActivate: [AuthenticatedGuard]},
  { path: 'reset-password/:token', component: ResetPasswordComponent, canActivate: [AuthenticatedGuard]},
  { path: 'email-validation/:token', component: EmailValidationComponent },
  { path: 'logout', component: LogoutComponent, canActivate: [AuthGuard] },

  { path: 'auth/social/google/callback', component: GoogleCallbackComponent, canActivate: [AuthenticatedGuard] },

  { path: '**', component: PageNotFoundComponent },
];
