package com.codeit.blob.user.repository;

import com.codeit.blob.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByOauthId(String oauthId);

    Optional<Users> findByBlobId(String blobId);

    Optional<Users> findByRefreshToken(String refreshToken);

    boolean existsByBlobId(String blobId);
}
