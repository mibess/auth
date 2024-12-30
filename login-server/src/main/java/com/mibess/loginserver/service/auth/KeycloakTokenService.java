package com.mibess.loginserver.service.auth;

import com.mibess.loginserver.config.FrontendProperties;
import com.mibess.loginserver.config.KeycloakProperties;
import com.mibess.loginserver.dto.TokenDTO;
import com.mibess.loginserver.exception.models.KeycloakException;
import com.mibess.loginserver.exception.models.ValidationException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakTokenService {

    private static final String GRANT_TYPE = "grant_type";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String CODE = "code";
    private static final String REDIRECT_URI = "redirect_uri";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private final KeycloakProperties keycloakProperties;
    private final FrontendProperties frontendProperties;

    private final RestTemplate restTemplate;

    public TokenDTO getTokenEmailPassword(String email, String password) {
        MultiValueMap<String, String> body = createRequestBody(PASSWORD, Map.of(
                USERNAME, email,
                PASSWORD, password
        ));
        return sendRequest(keycloakProperties.getTokenUrl(), body);
    }

    public TokenDTO getTokenFromGoogle(String code) {
        MultiValueMap<String, String> body = createRequestBody("authorization_code", Map.of(
                REDIRECT_URI, "%s/auth/social/google/callback".formatted(frontendProperties.getUrl()),
                CODE, code
        ));
        return sendRequest(keycloakProperties.getTokenUrl(), body);
    }

    public TokenDTO getRefreshToken(String refreshToken) {
        if (refreshToken.isBlank()){
            throw new ValidationException("Refresh token is empty");
        }
        MultiValueMap<String, String> body = createRequestBody(REFRESH_TOKEN, Map.of(
                REFRESH_TOKEN, refreshToken
        ));
        return sendRequest(keycloakProperties.getTokenUrl(), body);
    }

    public String getEmailFromToken(String token) {
        try {
            JWTClaimsSet claims = JWTParser.parse(token).getJWTClaimsSet();
            return claims.getStringClaim("email");
        } catch (Exception e) {
            log.error("Error extracting email from token", e);
            throw new KeycloakException("Failed to extract email from token", e);
        }
    }

    public void deleteToken(String refreshToken) {
        MultiValueMap<String, String> body = createRequestBody("", Map.of(REFRESH_TOKEN, refreshToken));
        sendDeleteRequest(keycloakProperties.getLogoutUrl(), body);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private MultiValueMap<String, String> createRequestBody(String grantType, Map<String, String> params) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        if (!grantType.isEmpty()) body.add(GRANT_TYPE, grantType);

        body.add("client_id", keycloakProperties.getClientId());
        body.add("client_secret", keycloakProperties.getClientSecret());
        params.forEach(body::add);

        return body;
    }

    private TokenDTO sendRequest(String url, MultiValueMap<String, String> body) {
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, createHeaders());
        try {
            ResponseEntity<ResponseTokenRequest> response = restTemplate.postForEntity(url, request, ResponseTokenRequest.class);

            if (response.getBody() == null) {
                throw new KeycloakException("Token request failed: empty response");
            }

            return convertToTokenDTO(response.getBody());
        } catch (HttpClientErrorException e) {
            log.error("Token request failed: {} ", e.getMessage());
            throw new ValidationException("Token request failed", e.getCause());
        }
    }

    private void sendDeleteRequest(String url, MultiValueMap<String, String> body) {
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, createHeaders());
        try {
            restTemplate.postForEntity(url, request, Void.class);
        } catch (Exception e) {
            log.error("Failed to delete token", e);
            throw new KeycloakException("Failed to delete token", e);
        }
    }

    private TokenDTO convertToTokenDTO(ResponseTokenRequest responseBody) {
        String accessToken = responseBody.access_token();
        String refreshToken = responseBody.refresh_token();

        if (accessToken == null || refreshToken == null) {
            throw new KeycloakException("Tokens not found in the response");
        }

        return new TokenDTO(accessToken, refreshToken);
    }


    private record ResponseTokenRequest(String access_token, String refresh_token) {
    }
}
