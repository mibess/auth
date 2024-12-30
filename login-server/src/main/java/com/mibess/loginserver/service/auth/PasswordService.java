package com.mibess.loginserver.service.auth;

import com.mibess.loginserver.dto.request.CreatePasswordRequest;
import lombok.extern.slf4j.Slf4j;

import com.mibess.loginserver.dto.request.ChangePasswordRequest;
import com.mibess.loginserver.dto.request.ResetPasswordRequest;
import com.mibess.loginserver.entity.UserEntity;
import com.mibess.loginserver.exception.models.ValidationException;
import com.mibess.loginserver.service.user.ResetPasswordService;
import com.mibess.loginserver.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordService {

    private final UserService userService;
    private final ResetPasswordService resetPasswordService;
    private final AuthService authService;
    private final KeycloakTokenService tokenService;

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        log.info("> Change password for email: {}", changePasswordRequest.email());

        var token = authService.login(changePasswordRequest.email(), changePasswordRequest.oldPassword());
        var user = userService.findUserEntityByEmail(changePasswordRequest.email());

        resetPasswordService.changePassword(user, changePasswordRequest.newPassword());

        tokenService.deleteToken(token.refreshToken());
        log.info("> Change password completed");
    }

    public void sendPasswordResetEmail(String email) {
        UserEntity userEntity = userService.findUserEntityByEmail(email);

        resetPasswordService.sendPasswordReset(userEntity);
    }

    public void validateResetPasswordToken(String token) {
        resetPasswordService.validateResetPasswordToken(token);
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        resetPasswordService.resetPassword(resetPasswordRequest);
    }

    public void createPassword(CreatePasswordRequest createPasswordRequest) {
        log.info("> Create password for email: {}", createPasswordRequest.email());

        UserEntity user = userService.findUserEntityByEmail(createPasswordRequest.email());

        if (user.isPasswordCreated()){
            throw new ValidationException("Your password has already been created");
        }
        user.setPasswordCreated(true);

        userService.updateUserEntity(user);

        resetPasswordService.createPassword(user, createPasswordRequest.password());
        log.info("> Create password completed");
    }
}
