package com.mibess.loginserver.service.email;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

public interface EmailService {

    void sendEmail(Message message);

    @Getter
    @Setter
    @Builder
    class Message {
        private Set<String> to;
        private String subject;
        private String template;
        private Map<String, Object> variables;
    }
}
