package com.paran.sample.domain.token.persistence.repository;

import com.paran.sample.domain.token.persistence.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long>, AccessTokenRepositoryCustom {
    Optional<AccessToken> findByToken(String token);

    Optional<AccessToken> findByRefreshToken(String refreshToken);
}
