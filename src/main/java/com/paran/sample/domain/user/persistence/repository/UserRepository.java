package com.paran.sample.domain.user.persistence.repository;

import com.paran.sample.domain.user.persistence.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByLoginId(String loginId);
}
