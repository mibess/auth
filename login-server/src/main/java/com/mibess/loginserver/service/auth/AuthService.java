package com.mibess.loginserver.service.auth;

import com.mibess.loginserver.dto.request.LogoutRequest;
import com.mibess.loginserver.dto.TokenDTO;
import com.mibess.loginserver.entity.ResetPasswordEntity;
import com.mibess.loginserver.service.user.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final KeycloakTokenService keycloakTokenService;
    private final ResetPasswordService resetPasswordService;

    public TokenDTO login(String email, String password) {
        log.info("Login attempt for email: {}", email);

        ResetPasswordEntity resetPasswordEntity = resetPasswordService.findByEmail(email);
        if (resetPasswordEntity != null) {
            resetPasswordService.delete(resetPasswordEntity);
        }
        return keycloakTokenService.getTokenEmailPassword(email, password);
    }

    public TokenDTO refreshToken(String refreshToken) {
        log.info("Refresh token attempt for refreshToken");
        return keycloakTokenService.getRefreshToken(refreshToken);
    }

    public void logout(LogoutRequest logoutRequest) {
        log.info("Logout attempt for refreshToken");
        keycloakTokenService.deleteToken(logoutRequest.refreshToken());
    }

}
