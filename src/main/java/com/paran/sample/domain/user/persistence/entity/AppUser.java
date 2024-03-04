package com.paran.sample.domain.user.persistence.entity;

import com.paran.sample.domain.common.persistence.entity.AbstractAuditable;
import com.paran.sample.domain.user.persistence.converter.EncryptConverter;
import com.paran.sample.domain.token.persistence.entity.AccessToken;
import com.paran.sample.domain.user.persistence.type.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AppUser extends AbstractAuditable implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appUserIdx;

    @Column(unique = true, nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Convert(converter = EncryptConverter.class)
    @Column(nullable = false)
    private String name;

    @Convert(converter = EncryptConverter.class)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @OneToMany(mappedBy = "user")
    private List<AccessToken> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}