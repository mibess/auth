package com.mibess.loginserver.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
public class PrivateController {
    private static final String SPACE = " \n\n ";
    @GetMapping
    public String privateEndpoint(@AuthenticationPrincipal Jwt jwt) {
        return "This is a private endpoint: " +
                SPACE +
                jwt.getClaim("email") +
                SPACE +
                jwt.getClaims().toString() +
                SPACE +
                jwt.getTokenValue();
    }

    @GetMapping("/jwt")
    public String privateJWT(@AuthenticationPrincipal Jwt jwt) {
        String claims = jwt.getClaims().toString();
        return "This is a private JWT Token: " + claims;
    }

}
