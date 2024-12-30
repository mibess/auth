package com.mibess.loginserver.controller;

import com.mibess.loginserver.config.KeycloakProperties;
import com.mibess.loginserver.dto.*;
import com.mibess.loginserver.dto.request.LoginRequest;
import com.mibess.loginserver.dto.request.*;
import com.mibess.loginserver.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final KeycloakProperties keycloakProperties;
    private final RestTemplate restTemplate;

    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest.email(), loginRequest.password());
    }

    @PostMapping("/refresh-token")
    public TokenDTO refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest.refreshToken());
    }

    @PostMapping("/logout")
    public void logout(@RequestBody LogoutRequest logout) {
        authService.logout(logout);
    }

}
