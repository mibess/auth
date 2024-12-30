package com.mibess.loginserver.converter;

import com.mibess.loginserver.dto.UserDTO;
import com.mibess.loginserver.dto.request.UserRequest;
import com.mibess.loginserver.dto.request.UserSocialRequest;
import com.mibess.loginserver.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserEntity toEntity(UserRequest user) {
        return populateUserEntity(
                user.name(),
                user.lastName(),
                user.email(),
                user.role().name()
        );
    }


    public UserEntity toEntity(UserSocialRequest user) {
        return populateUserEntity(
                user.name(),
                user.lastName(),
                user.email(),
                user.role().name()
        );
    }

    public UserDTO toDTO(UserEntity user) {
        return new UserDTO(
                user.getKeycloakId(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getSocialLogin(),
                user.getSocialLogin() != null && !user.isPasswordCreated(),
                user.isEmailVerified()
        );
    }

    private UserEntity populateUserEntity(String name, String lastName, String email, String role) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(name);
        userEntity.setLastName(lastName);
        userEntity.setEmail(email);
        userEntity.setRole(role);
        return userEntity;
    }

}
