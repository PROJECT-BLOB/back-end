package com.codeit.blob.oauth.controller;

import com.codeit.blob.jwt.exception.JwtExpiredException;
import com.codeit.blob.jwt.provider.JwtProvider;
import com.codeit.blob.oauth.OauthType;
import com.codeit.blob.oauth.dto.UrlResponse;
import com.codeit.blob.oauth.response.OauthResponse;
import com.codeit.blob.oauth.service.OauthManager;
import com.codeit.blob.oauth.service.OauthService;
import com.codeit.blob.user.domain.Users;
import com.codeit.blob.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Tag(name = "Oauth API", description = "Oauth 로그인 API")
public class OauthController {

    private final OauthManager manager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;


    @GetMapping("/{type}")
    @Operation(summary = "Oauth Login Page Url 리턴")
    public ResponseEntity<UrlResponse> createLoginPage(
            @PathVariable("type") String oauthType
    ) {
        OauthType type = OauthType.toOauthType(oauthType);
        OauthService oauthService = manager.getService(type);
        return ResponseEntity.ok(new UrlResponse(oauthService.createLoginUrl()));
    }

    @GetMapping("/{type}/callback")
    @Operation(hidden = true)
    public ResponseEntity<Object> callback(
            @PathVariable(name = "type") String oauthType,
            @RequestParam(name = "code") String code
    ) {
        OauthType type = OauthType.toOauthType(oauthType);
        OauthService oauthService = manager.getService(type);
        OauthResponse tokenResponse = oauthService.createToken(code);

        return ResponseEntity.ok()
                .body(tokenResponse);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh Token 으로 Access Token 재발급")
    public ResponseEntity<Object> refresh(
            HttpServletRequest request
    ) {
        //TODO KDY 리팩토링 필요
        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!jwtProvider.isTokenExpired(refreshToken)) {
            Users users = userRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(JwtExpiredException::new);

            if (jwtProvider.isTokenValid(refreshToken, users)) {
                Map<String, Object> extractClaims = new HashMap<>();
                extractClaims.put("oauthId", users.getOauthId());
                return ResponseEntity.ok(jwtProvider.generateAccessToken(extractClaims));
            }
        }

        throw new IllegalArgumentException("Access Token 재발급 실패");
    }
}
