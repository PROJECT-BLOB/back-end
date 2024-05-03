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
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Transactional
    public TokenResponse createAccessToken(String token) {
        String refreshToken = token.substring(JwtProvider.JWT_PREFIX.length());

        if (!jwtProvider.isTokenExpired(refreshToken)) {
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

    @Transactional
    public String deleteRefreshToken(String token) {
        String accessToken = token.substring(JwtProvider.JWT_PREFIX.length());
        if (!jwtProvider.isTokenExpired(accessToken)) {
            String oauthId = jwtProvider.extractClaim(accessToken, claims -> claims.get("oauthId", String.class));
            Users users = userRepository.findByOauthId(oauthId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            users.changeUser(
                    users.toBuilder()
                            .refreshToken("")
                            .build()
            );

            userRepository.save(users);
        }

        return "로그아웃 성공";
    }
}
