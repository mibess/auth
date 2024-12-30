package com.mibess.loginserver.service.auth;

import com.mibess.loginserver.exception.models.BusinessException;
import com.mibess.loginserver.exception.models.ValidationException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import com.mibess.loginserver.config.FrontendProperties;
import com.mibess.loginserver.config.KeycloakProperties;
import com.mibess.loginserver.dto.TokenDTO;
import com.mibess.loginserver.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialAuthService {

    private final KeycloakProperties keycloakProperties;
    private final FrontendProperties frontendProperties;
    private final KeycloakTokenService tokenService;
    private final UserService userService;

    public TokenDTO loginGoogle(String code) {
        var token = tokenService.getTokenFromGoogle(code);
        var email = tokenService.getEmailFromToken(token.accessToken());
        userService.registerBySocial(email);
        log.info("Login attempt for email: {}", email);
        return token;
    }

    public void generateGoogleLoginUrl(HttpServletResponse response) {
        log.info("Generating Google login URL");
        var responseURL = String.format(
                "%s/realms/%s/protocol/openid-connect/auth?client_id=%s&redirect_uri=%s&response_type=code&scope=openid email profile&kc_idp_hint=google",
                keycloakProperties.getAuthServerUrl(),
                keycloakProperties.getRealm(),
                keycloakProperties.getClientId(),
                "%s/auth/social/google/callback".formatted(frontendProperties.getUrl())
        );

        try {
            response.sendRedirect(responseURL);
        } catch (Exception e) {
            log.error("Error while redirecting to Google login URL", e);
            throw new BusinessException("Error while redirecting to Google login URL");
        }
    }
}
