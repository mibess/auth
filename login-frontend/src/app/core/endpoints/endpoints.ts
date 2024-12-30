import { environment } from "../../../environments/environment";


export const API_ENDPOINTS = {
  BASE_URL: environment.API_BASE_URL,
  // auth
  LOGIN: `${environment.API_BASE_URL}/auth/login`,
  REGISTER: `${environment.API_BASE_URL}/auth/register`,
  GOOGLE: `${environment.API_BASE_URL}/auth/social/google`,
  GOOGLE_CALLBACK: `${environment.API_BASE_URL}/auth/social/google/callback`,
  INTROSPECT: `${environment.API_BASE_URL}/auth/introspect`,
  REFRESH_TOKEN: `${environment.API_BASE_URL}/auth/refresh-token`,
  CHANGE_PASSWORD: `${environment.API_BASE_URL}/auth/password/change`,
  CREATE_PASSWORD: `${environment.API_BASE_URL}/auth/password/create`,
  FORGOT_PASSWORD: `${environment.API_BASE_URL}/auth/password/forgot`,
  VALIDATE_RESET_PASSWORD_TOKEN: `${environment.API_BASE_URL}/auth/password/validate-token`,
  RESET_PASSWORD: `${environment.API_BASE_URL}/auth/password/reset`,
  VALIDATE_EMAIL: `${environment.API_BASE_URL}/auth/email/validate`,
  LOGOUT: `${environment.API_BASE_URL}/auth/logout`,
};
