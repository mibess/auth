package com.mibess.loginserver.service.auth;


import com.mibess.loginserver.config.KeycloakProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    public void assignRoleToUser(String userId, String roleName) {
        UserResource userResource = keycloak.realm(keycloakProperties.getRealm()).users().get(userId);
        RolesResource rolesResource = keycloak.realm(keycloakProperties.getRealm()).roles();

        RoleRepresentation role = rolesResource.get(roleName).toRepresentation();
        userResource.roles().realmLevel().remove(userResource.roles().realmLevel().listAll());
        userResource.roles().realmLevel().add(List.of(role));

        log.info("Role {} assigned to user {}", roleName, userId);
    }
}
