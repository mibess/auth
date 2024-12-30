package com.mibess.loginserver.config;

import com.mibess.loginserver.service.email.EmailService;
import com.mibess.loginserver.service.email.FakeEmailService;
import com.mibess.loginserver.service.email.SmtpEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EmailConfig {

    private final EmailProperties emailProperties;

    @Bean
    public EmailService emailService() {
        if ("smtp".equals(emailProperties.getMode())) {
            return new SmtpEmailService();
        }

        return new FakeEmailService(emailProperties);
    }

}
