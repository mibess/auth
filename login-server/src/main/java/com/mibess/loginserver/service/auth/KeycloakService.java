package com.mibess.loginserver.service.auth;

import com.mibess.loginserver.config.KeycloakProperties;
import com.mibess.loginserver.dto.request.UserRequest;
import com.mibess.loginserver.entity.UserEntity;
import com.mibess.loginserver.exception.models.KeycloakException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;
    private final RoleService roleService;

    public String saveOnKeycloak(UserRequest userRequest) {
        UserRepresentation userRepresentation = buildUserRepresentation(userRequest);

        UsersResource usersResource = getUsersResource();
        try (Response response = usersResource.create(userRepresentation)) {
            if (response.getStatus() != 201) {
                throw new KeycloakException("Error creating user in Keycloak: " + response.getStatus());
            }
        }

        String userId = usersResource.searchByEmail(userRequest.email(), true).getFirst().getId();

        resetPassword(userId, userRequest.password());

        roleService.assignRoleToUser(userId, userRequest.role().name());

        log.info("User {} created in Keycloak successfully", userRequest.email());
        return userId;
    }

    public void updateOnKeycloak(UserEntity userEntity) {
        UserResource userResource = getUserResource(userEntity.getKeycloakId());

        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setUsername(userEntity.getEmail());
        userRepresentation.setFirstName(userEntity.getName());
        userRepresentation.setLastName(userEntity.getLastName());
        userRepresentation.setEmail(userEntity.getEmail());
        userRepresentation.setEmailVerified(userEntity.isEmailVerified());

        userResource.update(userRepresentation);
        roleService.assignRoleToUser(userEntity.getKeycloakId(), userEntity.getRole());

        log.info("User {} updated successfully", userEntity.getEmail());
    }

    public void deleteOnKeycloak(String keycloakId) {
        try (Response response = getUsersResource().delete(keycloakId)){
            if (response.getStatus() != 204) {
                throw new KeycloakException("Error deleting user in Keycloak: " + response.getStatus());
            }
        }

        log.info("User with ID {} deleted from Keycloak", keycloakId);
    }

    public void disableUser(String keycloakId) {
        UserResource userResource = getUserResource(keycloakId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEnabled(false);
        userResource.update(userRepresentation);
        log.info("User {} disabled successfully", keycloakId);
    }

    public void resetPassword(String keycloakId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        getUserResource(keycloakId).resetPassword(credential);
        log.info("Password reset for user ID {}", keycloakId);
    }

    public UserRepresentation findByEmail(String email) {
        if (email == null || email.isEmpty()){
            throw new KeycloakException("Email cant be null or empty");
        }

        UsersResource userResource = keycloak.realm(keycloakProperties.getRealm()).users();
        return userResource.search(email).getFirst();
    }

    private UserRepresentation buildUserRepresentation(UserRequest userRequest) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userRequest.email());
        user.setEmail(userRequest.email());
        user.setFirstName(userRequest.name());
        user.setLastName(userRequest.lastName());
        user.setEnabled(true);
        return user;
    }

    private UsersResource getUsersResource() {
        return keycloak.realm(keycloakProperties.getRealm()).users();
    }

    private UserResource getUserResource(String userId) {
        return getUsersResource().get(userId);
    }
}
