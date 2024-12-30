package com.mibess.loginserver.service.email;

import com.mibess.loginserver.config.EmailProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Service
public class FakeEmailService implements EmailService {

    private EmailProperties emailProperties;

    @Override
    public void sendEmail(Message message) {
        log.info("Email from: {}", emailProperties.getFrom());
        log.info("Email sent to: {}", message.getTo());
        log.info("Subject: {}", message.getSubject());
        log.info("Template: {}", message.getTemplate());
        log.info("Variables: {}", message.getVariables());
    }
}
