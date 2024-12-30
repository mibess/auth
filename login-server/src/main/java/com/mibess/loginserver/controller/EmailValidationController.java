package com.mibess.loginserver.controller;

import com.mibess.loginserver.service.auth.EmailValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/email/validate")
public class EmailValidationController {

    private final EmailValidationService verifyEmailService;

    @GetMapping("{token}")
    public void confirmEmailValidation(@PathVariable String token) {
        verifyEmailService.confirmEmailValidation(token);
    }

}
