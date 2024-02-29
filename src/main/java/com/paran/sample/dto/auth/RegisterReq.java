package com.paran.sample.dto.auth;

import com.paran.sample.persistence.entity.user.User;
import com.paran.sample.persistence.type.user.RoleType;

import static java.util.Objects.requireNonNullElse;

public record RegisterReq(String loginId, String password, String name, String phone, RoleType role) {

    public RegisterReq {
        role = requireNonNullElse(role, RoleType.USER);
    }

    public User toUserEntity(String encPassword) {
        return User.builder()
                .loginId(loginId)
                .password(encPassword)
                .name(name)
                .phone(phone)
                .role(role)
                .build();
    }
}
