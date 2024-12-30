package com.mibess.loginserver.service.user;

import com.mibess.loginserver.config.FrontendProperties;
import com.mibess.loginserver.dto.request.ResetPasswordRequest;
import com.mibess.loginserver.entity.ResetPasswordEntity;
import com.mibess.loginserver.entity.UserEntity;
import com.mibess.loginserver.repository.ResetPasswordRepository;
import com.mibess.loginserver.service.auth.TokenService;
import com.mibess.loginserver.service.email.EmailBuilder;
import com.mibess.loginserver.service.auth.KeycloakService;
import com.mibess.loginserver.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {
    private final FrontendProperties frontendProperties;
    private final ResetPasswordRepository resetPasswordRepository;
    private final EmailService emailService;
    private final KeycloakService keycloakService;
    private final TokenService tokenService;
    private final EmailBuilder emailBuilder;

    public void sendPasswordReset(UserEntity userEntity) {
        ResetPasswordEntity resetPasswordEntity = resetPasswordRepository.findByUser(userEntity);

        if (resetPasswordEntity != null && tokenService.isTokenValid(resetPasswordEntity.getExpirationDate())) {
            return;
        }

        if (resetPasswordEntity != null) {
            resetPasswordRepository.delete(resetPasswordEntity);
            resetPasswordRepository.flush();
        }

        String token = tokenService.generateToken();

        resetPasswordEntity = new ResetPasswordEntity();
        resetPasswordEntity.setToken(token);
        resetPasswordEntity.setUser(userEntity);
        resetPasswordEntity.setExpirationDate(LocalDateTime.now().plusHours(1));

        resetPasswordRepository.save(resetPasswordEntity);

        var resetPasswordUrl = "%s/reset-password/%s".formatted(frontendProperties.getUrl(), token);

        var message = emailBuilder.buildResetPasswordMessage(
                userEntity.getEmail(),
                resetPasswordUrl);

        emailService.sendEmail(message);
    }

    public void changePassword(UserEntity userEntity, String newPassword) {
        keycloakService.resetPassword(userEntity.getKeycloakId(), newPassword);
    }

    public void createPassword(UserEntity userEntity, String newPassword) {
        keycloakService.resetPassword(userEntity.getKeycloakId(), newPassword);
    }

    public void validateResetPasswordToken(String token) {
        ResetPasswordEntity resetPasswordEntity = resetPasswordRepository.findByToken(token).orElseThrow(
                () -> new RuntimeException("Invalid token or token expired")
        );

        tokenService.validateToken(resetPasswordEntity.getExpirationDate());
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        ResetPasswordEntity resetPasswordEntity = this.findByToken(resetPasswordRequest.token());

        keycloakService.resetPassword(resetPasswordEntity.getUser().getKeycloakId(), resetPasswordRequest.newPassword());

        resetPasswordRepository.delete(resetPasswordEntity);
    }

    public void deleteResetPasswordByUser(UserEntity user) {
        ResetPasswordEntity resetPasswordEntity = resetPasswordRepository.findByUser(user);
        if (resetPasswordEntity == null){
            return;
        }
        resetPasswordRepository.delete(resetPasswordEntity);
    }

    public ResetPasswordEntity findByEmail(String email) {
        return resetPasswordRepository.findByUserEmail(email).orElse(null);
    }

    private ResetPasswordEntity findByToken(String token) {
        return resetPasswordRepository.findByToken(token).orElseThrow(
                () -> new RuntimeException("Invalid token or token expired")
        );
    }

    public void delete(ResetPasswordEntity resetPasswordEntity) {
        resetPasswordRepository.delete(resetPasswordEntity);
    }
}
