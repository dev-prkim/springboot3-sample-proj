package com.paran.sample.domain.token.persistence.entity;

import com.paran.sample.domain.common.persistence.entity.AbstractAuditable;
import com.paran.sample.domain.user.persistence.entity.AppUser;
import com.paran.sample.domain.token.persistence.type.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccessToken extends AbstractAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long accessTokenIdx;

    @Column(unique = true, nullable = false)
    public String token;
    public String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    public TokenType tokenType = TokenType.BEARER;

    @Setter
    @Column(nullable = false)
    public boolean revoked;

    @Setter
    @Column(nullable = false)
    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_idx", nullable = false)
    public AppUser user;
}
