package com.paran.sample.domain.auth.dto;

import com.paran.sample.domain.user.persistence.entity.AppUser;
import com.paran.sample.domain.user.persistence.type.RoleType;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

public record RegisterReq(String loginId, String password, String name, String phone, RoleType role) {

    public RegisterReq {
        // null check
        validateInput(loginId, "loginId is required.");
        validateInput(password, "password is required.");
        validateInput(name, "name is required.");

        // set default value
        role = requireNonNullElse(role, RoleType.USER);
    }

    private void validateInput(String input, String errorMessage) {
        requireNonNull(input, errorMessage);
        if (input.isBlank()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public AppUser toUserEntity(String encPassword) {
        return AppUser.builder()
                .loginId(loginId)
                .password(encPassword)
                .name(name)
                .phone(phone)
                .role(role)
                .build();
    }
}
