package com.mibess.loginserver.repository;

import com.mibess.loginserver.entity.EmailValidationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailValidationRepository extends JpaRepository<EmailValidationEntity, Long> {
    Optional<EmailValidationEntity> findByToken(String token);

    EmailValidationEntity findByUserEmail(String email);
}
