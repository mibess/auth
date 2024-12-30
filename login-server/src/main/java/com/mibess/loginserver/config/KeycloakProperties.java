package com.mibess.loginserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {

    private String realm;
    private String clientId;
    private String clientSecret;
    private String authServerUrl;
    private String tokenUrl;
    private String logoutUrl;
    private String userInfoUrl;
    private String introspectUrl;
    private String username;
    private String password;

}
