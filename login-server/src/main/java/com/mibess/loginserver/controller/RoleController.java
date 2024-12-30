package com.mibess.loginserver.controller;

import com.mibess.loginserver.dto.RoleDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @GetMapping("admin")
    public RoleDTO admin() {
        return new RoleDTO("You are ADMIN and can access this endpoint");
    }

    @GetMapping("user")
    public RoleDTO user(@AuthenticationPrincipal Jwt jwt) {
        return new RoleDTO("%s, You are USER or ADMIN and can access this endpoint".formatted(jwt.getClaim("name").toString()));
    }
}
