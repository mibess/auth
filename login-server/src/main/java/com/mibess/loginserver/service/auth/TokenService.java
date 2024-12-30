package com.mibess.loginserver.service.auth;

import com.mibess.loginserver.exception.models.BusinessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenService {

    public boolean isTokenValid(LocalDateTime expirationDate) {
        return LocalDateTime.now().isBefore(expirationDate);
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void validateToken(LocalDateTime expirationDate) {
        if (!isTokenValid(expirationDate)) {
            throw new BusinessException("Token expired");
        }
    }
}
