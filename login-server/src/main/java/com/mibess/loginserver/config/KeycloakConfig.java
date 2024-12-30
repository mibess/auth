package com.mibess.loginserver.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {

    private final KeycloakProperties keycloakProperties;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getAuthServerUrl())
                .realm("master") // Use "master" to access any realm
                .clientId("admin-cli")
                .username(keycloakProperties.getUsername()) // Keycloak admin user
                .password(keycloakProperties.getPassword()) // Keycloak admin password
                .build();
    }
}
