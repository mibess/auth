package com.mibess.loginserver.service.email;

import com.mibess.loginserver.config.EmailProperties;
import com.mibess.loginserver.exception.models.BusinessException;
import freemarker.template.Template;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Service
public class SmtpEmailService implements EmailService {

    private EmailProperties emailProperties;
    private JavaMailSender mailSender;
    private FreeMarkerConfig freemarkerConfig;

    @Override
    public void sendEmail(Message message) {
        try {
            MimeMessage mimeMessage = createMimeMessage(message);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new BusinessException("Failed to send email", e);
        }
    }

    private MimeMessage createMimeMessage(Message message) throws MessagingException {
        String template = processTemplate(message);

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        helper.setFrom(emailProperties.getFrom());
        helper.setTo(message.getTo().toArray(new String[0]));
        helper.setSubject(message.getSubject());
        helper.setText(template, true);

        return mimeMessage;

    }

    private String processTemplate(Message message) {
        try {
            Template template = freemarkerConfig.getConfiguration().getTemplate(message.getTemplate());

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, message.getVariables());
        } catch (Exception e) {
            log.error("Error create template", e);
            throw new BusinessException("Error create template", e);
        }
    }
}
