package com.mibess.loginserver.service.email;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class EmailBuilder {

    public EmailService.Message buildResetPasswordMessage(String email, String resetPasswordUrl){
        var subject = "Reset your password";
        var template = "reset-password.html";

        Map<String, Object> variables = Map.of("resetPasswordUrl", resetPasswordUrl);

        return EmailService.Message.builder()
                .to(Set.of(email))
                .subject(subject)
                .template(template)
                .variables(variables)
                .build();
    }

    public EmailService.Message buildEmailValidationMessage(String email, String emailValidationUrl){
        var subject = "Hey! Confirm your email and enjoy the app!";
        var template = "email-validation.html";

        Map<String, Object> variables = Map.of("emailValidationUrl", emailValidationUrl);

        return EmailService.Message.builder()
                .to(Set.of(email))
                .subject(subject)
                .template(template)
                .variables(variables)
                .build();
    }
}
