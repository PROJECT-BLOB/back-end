package com.codeit.blob.oauth.service;

import com.codeit.blob.jwt.provider.JwtProvider;
import com.codeit.blob.user.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtProvider jwtProvider;

    public String createRefreshToken(Users users) {
        Map<String, Object> extractClaims = new HashMap<>();
        extractClaims.put("oauthId", users.getOauthId());

        return jwtProvider.generateRefreshToken(extractClaims);
    }
}
