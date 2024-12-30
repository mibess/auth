package com.mibess.loginserver.dto;

public record UserDTO (
        String id,
        String name,
        String lastName,
        String email,
        String role,
        String social,
        boolean canCreatePassword,
        boolean emailVerified) {
}
