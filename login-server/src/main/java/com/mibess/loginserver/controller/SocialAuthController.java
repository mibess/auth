package com.mibess.loginserver.controller;

import com.mibess.loginserver.dto.TokenDTO;
import com.mibess.loginserver.dto.request.CodeRequest;
import com.mibess.loginserver.service.auth.SocialAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/social")
public class SocialAuthController {

    private final SocialAuthService socialAuthService;

    @GetMapping("/google")
    public void googleLoginUrl(HttpServletResponse response) {
        socialAuthService.generateGoogleLoginUrl(response);
    }

    @PostMapping("/google/callback")
    public ResponseEntity<TokenDTO> googleCallback(@RequestBody CodeRequest codeRequest) {
        return ResponseEntity.ok(socialAuthService.loginGoogle(codeRequest.code()));
    }

}
