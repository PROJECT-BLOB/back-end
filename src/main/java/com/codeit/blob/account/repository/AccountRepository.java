package com.codeit.blob.account.repository;

import com.codeit.blob.account.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByOauthId(String oauthId);
}
