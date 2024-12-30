package com.mibess.loginserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults()) // Cross-Origin Resource Sharing (using token on requisitions)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                    authorizeRequests -> {
                        authorizeRequests.requestMatchers("/users/**", "/public", "/auth/**").permitAll();

                        authorizeRequests.requestMatchers("/roles/admin").hasRole(ROLE_ADMIN);
                        authorizeRequests.requestMatchers("/roles/user").hasAnyRole(ROLE_ADMIN, ROLE_USER);

                        authorizeRequests.requestMatchers(HttpMethod.PUT, "/users/{id}").hasRole(ROLE_ADMIN);
                        authorizeRequests.requestMatchers(HttpMethod.PUT, "/users/{id}/unable").hasRole(ROLE_ADMIN);
                        authorizeRequests.requestMatchers(HttpMethod.DELETE, "/users/{id}").hasRole(ROLE_ADMIN);

                        authorizeRequests.anyRequest().authenticated();
                    }
                )
                .oauth2Login(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(new JWTConverter()))) // Cross-Origin Resource Sharing (using token on requisitions)
                        //oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

}
