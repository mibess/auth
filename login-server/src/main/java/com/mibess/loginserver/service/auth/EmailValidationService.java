package com.mibess.loginserver.service.auth;

import com.mibess.loginserver.config.FrontendProperties;
import com.mibess.loginserver.entity.UserEntity;
import com.mibess.loginserver.entity.EmailValidationEntity;
import com.mibess.loginserver.exception.models.ValidationException;
import com.mibess.loginserver.repository.EmailValidationRepository;
import com.mibess.loginserver.repository.UserRepository;
import com.mibess.loginserver.service.email.EmailBuilder;
import com.mibess.loginserver.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailValidationService {

    private final EmailValidationRepository emailValidationRepository;
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final FrontendProperties frontendProperties;
    private final EmailBuilder emailBuilder;

    public void sendEmailValidation(UserEntity user){
        EmailValidationEntity emailValidationEntity = emailValidationRepository.findByUserEmail(user.getEmail());

        if (emailValidationEntity != null && tokenService.isTokenValid(emailValidationEntity.getExpirationDate())) {
            return;
        }

        if (emailValidationEntity != null) {
            emailValidationRepository.delete(emailValidationEntity);
            emailValidationRepository.flush();
        }

        String token = tokenService.generateToken();

        emailValidationEntity = new EmailValidationEntity();
        emailValidationEntity.setToken(token);
        emailValidationEntity.setUser(user);
        emailValidationEntity.setExpirationDate(LocalDateTime.now().plusDays(1));

        emailValidationRepository.save(emailValidationEntity);

        var emailValidationUrl = "%s/email-validation/%s".formatted(frontendProperties.getUrl(), token);

        var message = emailBuilder.buildEmailValidationMessage(
                user.getEmail(),
                emailValidationUrl);

        emailService.sendEmail(message);
    }

    public void confirmEmailValidation(String token){

        EmailValidationEntity verifyEmailEntity = emailValidationRepository.findByToken(token).orElseThrow(
                () -> new ValidationException("Code validation not found!")
        );

        UserEntity userEntity = verifyEmailEntity.getUser();
        userEntity.setEmailVerified(true);

        userRepository.save(userEntity);
        keycloakService.updateOnKeycloak(userEntity);

        emailValidationRepository.delete(verifyEmailEntity);
    }

    public void deleteEmailValidationByUser(UserEntity userEntity) {
        EmailValidationEntity emailValidationEntity = emailValidationRepository.findByUserEmail(userEntity.getEmail());

        if (emailValidationEntity == null){
            return;
        }

        emailValidationRepository.delete(emailValidationEntity);
    }
}
