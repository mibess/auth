package com.mibess.loginserver.service.user;

import com.mibess.loginserver.converter.UserConverter;
import com.mibess.loginserver.dto.UserDTO;
import com.mibess.loginserver.dto.request.UserRequest;
import com.mibess.loginserver.dto.request.UserSocialRequest;
import com.mibess.loginserver.dto.request.UserUpdateRequest;
import com.mibess.loginserver.entity.UserEntity;
import com.mibess.loginserver.entity.enums.Role;
import com.mibess.loginserver.exception.models.ValidationException;
import com.mibess.loginserver.repository.UserRepository;
import com.mibess.loginserver.service.auth.KeycloakService;
import com.mibess.loginserver.service.auth.EmailValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final KeycloakService keycloakService;
    private final ResetPasswordService resetPasswordService;
    private final EmailValidationService emailValidationService;

    public UserDTO register(UserRequest user) {
        UserEntity userEntity = userConverter.toEntity(user);
        userEntity.setEnabled(true);
        userEntity.setPasswordCreated(true);

        String keycloakId = keycloakService.saveOnKeycloak(user);
        userEntity.setKeycloakId(keycloakId);

        userRepository.save(userEntity);

        emailValidationService.sendEmailValidation(userEntity);

        log.info("User registered: {} - {}", userEntity.getName(), userEntity.getEmail());
        return userConverter.toDTO(userEntity);
    }

    public void registerBySocial(String email) {
        if (userRepository.findByEmail(email) != null){
            return;
        }

        UserRepresentation userRepresentation = keycloakService.findByEmail(email);

        UserSocialRequest userRequest = new UserSocialRequest(
                userRepresentation.getId(),
                userRepresentation.getFirstName(),
                userRepresentation.getLastName(),
                userRepresentation.getEmail(),
                Role.USER
        );

        UserEntity userEntity = userConverter.toEntity(userRequest);
        userEntity.setKeycloakId(userRequest.keycloakId());
        userEntity.setEnabled(true);
        userEntity.setSocialLogin("google");

        userRepository.save(userEntity);

        keycloakService.updateOnKeycloak(userEntity);

        emailValidationService.sendEmailValidation(userEntity);

        log.info("User registered by social: {} - {}", userEntity.getName(), userEntity.getEmail());
    }

    public UserDTO update(String keycloakId, UserUpdateRequest user) {
        UserEntity userEntity = userRepository.findByKeycloakId(keycloakId).orElseThrow();

        userEntity.setName(user.name());
        userEntity.setLastName(user.lastName());
        userEntity.setEmail(user.email());
        userEntity.setRole(user.role().name());

        userRepository.save(userEntity);

        keycloakService.updateOnKeycloak(userEntity);
        log.info("User updated: {} - {}", userEntity.getName(), userEntity.getEmail());
        return userConverter.toDTO(userEntity);
    }

    public void updateUserEntity(UserEntity userEntity){
        userRepository.save(userEntity);
    }

    public void delete(String keycloakId) {
        UserEntity userEntity = this.findByKeycloakId(keycloakId);
        resetPasswordService.deleteResetPasswordByUser(userEntity);
        emailValidationService.deleteEmailValidationByUser(userEntity);
        userRepository.delete(userEntity);
        keycloakService.deleteOnKeycloak(keycloakId);
        log.info("User deleted: {} - {}", userEntity.getName(), userEntity.getEmail());
    }

    public void disable(String keycloakId) {
        UserEntity userEntity = this.findByKeycloakId(keycloakId);
        userEntity.setEnabled(false);

        userRepository.save(userEntity);

        keycloakService.disableUser(keycloakId);
        log.info("User disabled: {} - {}", userEntity.getName(), userEntity.getEmail());
    }

    public UserDTO findById(String keycloakId) {
        UserEntity userEntity = this.findByKeycloakId(keycloakId);
        return userConverter.toDTO(userEntity);
    }

    public UserDTO findByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        return userConverter.toDTO(userEntity);
    }

    public List<UserDTO> findAll() {
        List<UserEntity> users = userRepository.findAll();

        return users.stream()
                .map(userConverter::toDTO)
                .toList();
    }

    public UserEntity findByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId).orElseThrow(
                () -> new ValidationException("User id not found: %s".formatted(keycloakId))
        );
    }

    public UserEntity findUserEntityByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            log.error("User not found with email: {}", email);
            throw new ValidationException("User not found with email: " + email);
        }

        return userEntity;
    }
}
