package com.mibess.loginserver.repository;

import com.mibess.loginserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);
    Optional<UserEntity> findByKeycloakId(String keycloakId);

}
