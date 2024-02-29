package com.paran.sample.persistence.entity.token;

import com.paran.sample.persistence.entity.user.User;
import com.paran.sample.persistence.type.token.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccessToken {

    @Id
    @GeneratedValue
    public Long accessTokenIdx;

    @Column(unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    public TokenType tokenType = TokenType.BEARER;

    @Setter
    public boolean revoked;

    @Setter
    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    public User user;
}
