package com.mibess.loginserver.controller;

import com.mibess.loginserver.dto.request.ChangePasswordRequest;
import com.mibess.loginserver.dto.request.CreatePasswordRequest;
import com.mibess.loginserver.dto.request.ForgotPasswordRequest;
import com.mibess.loginserver.dto.request.ResetPasswordRequest;
import com.mibess.loginserver.service.auth.PasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/password")
public class PasswordController {
    private final PasswordService passwordService;

    @PostMapping("/change")
    public void changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        passwordService.changePassword(changePasswordRequest);
    }

    @PostMapping("/forgot")
    public void forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        passwordService.sendPasswordResetEmail(forgotPasswordRequest.email());
    }

    @PostMapping("/create")
    public void createPassword(@RequestBody CreatePasswordRequest createPasswordRequest) {
        passwordService.createPassword(createPasswordRequest);
    }

    @GetMapping("/validate-token/{token}")
    public void validateResetPasswordToken(@PathVariable String token) {
        passwordService.validateResetPasswordToken(token);
    }

    @PostMapping("/reset")
    public void resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        passwordService.resetPassword(resetPasswordRequest);
    }

}
