package com.mibess.loginserver.repository;

import com.mibess.loginserver.entity.ResetPasswordEntity;
import com.mibess.loginserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPasswordEntity, Long> {
    Optional<ResetPasswordEntity> findByToken(String token);

    ResetPasswordEntity findByUser(UserEntity userEntity);

    Optional<ResetPasswordEntity> findByUserEmail(String email);
}
