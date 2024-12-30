export type IntrospectRequest = {
  token: string;
}

export type IntrospectResponse = {
  exp: number;
  sub: string;
  realmAccess: {
    roles: string[];
  };
  emailVerified: boolean;
  name: string;
  preferredUsername: string;
  givenName: string;
  familyName: string;
  email: string;
  active: boolean;
};
