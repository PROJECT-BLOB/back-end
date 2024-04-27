package com.codeit.blob.oauth.service;

import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;
import com.codeit.blob.jwt.provider.JwtProvider;
import com.codeit.blob.oauth.response.TokenResponse;
import com.codeit.blob.user.domain.Users;
import com.codeit.blob.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Transactional
    public TokenResponse createAccessToken(String refreshToken) {
        if (!jwtProvider.isTokenExpired(refreshToken)) {
            List<Users> all = userRepository.findAll();
            Users users1 = all.get(0);
            log.info(users1.getRefreshToken());

            Users users = userRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            if (jwtProvider.isTokenValid(refreshToken, users)) {
                Map<String, Object> extractClaims = new HashMap<>();
                extractClaims.put("oauthId", users.getOauthId());
                return new TokenResponse(jwtProvider.generateAccessToken(extractClaims));
            }
        }

        throw new IllegalArgumentException("Access Token 재발급 실패");
    }
}
