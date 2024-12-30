export type UserRegister = {
  name: string;
  lastName: string;
  email: string;
  password: string;
  role: string;
}

export type UserUpdateRequest = {
  name: string;
  lastName: string;
  email: string;
  role: string;
}

export type UserResponse = {
  id: string;
  name: string;
  lastName: string;
  email: string;
  role: string;
  social: string;
  canCreatePassword: boolean;
  emailVerified: boolean;
}

export interface UserSession {
  id: string;
  name: string;
  lastName: string;
  email: string;
  role: string;
}
