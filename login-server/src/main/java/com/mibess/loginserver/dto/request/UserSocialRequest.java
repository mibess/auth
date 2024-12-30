package com.mibess.loginserver.dto.request;

import com.mibess.loginserver.entity.enums.Role;

public record UserSocialRequest(String keycloakId, String name, String lastName, String email, Role role) {
}
