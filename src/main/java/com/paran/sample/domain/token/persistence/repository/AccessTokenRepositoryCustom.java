package com.paran.sample.domain.token.persistence.repository;

import com.paran.sample.domain.token.persistence.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccessTokenRepositoryCustom {
    List<AccessToken> findAccessTokensByUserIdx(Long appUserIdx);
}
