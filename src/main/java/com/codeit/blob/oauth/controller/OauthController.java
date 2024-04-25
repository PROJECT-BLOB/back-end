package com.codeit.blob.oauth.controller;

import com.codeit.blob.jwt.provider.JwtProvider;
import com.codeit.blob.oauth.OauthType;
import com.codeit.blob.oauth.response.LoginPageResponse;
import com.codeit.blob.oauth.response.OauthResponse;
import com.codeit.blob.oauth.response.TokenResponse;
import com.codeit.blob.oauth.service.OauthManager;
import com.codeit.blob.oauth.service.OauthService;
import com.codeit.blob.oauth.service.TokenService;
import com.codeit.blob.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Tag(name = "Oauth API", description = "Oauth 로그인 API")
public class OauthController {

    private final OauthManager manager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @GetMapping("/{type}")
    @Operation(summary = "Oauth Redirect Url API", description = "type 에 해당하는 redirect url 주소를 리턴합니다.")
    public ResponseEntity<LoginPageResponse> createLoginPage(
            @PathVariable("type") String oauthType
    ) {
        OauthType type = OauthType.toOauthType(oauthType);
        OauthService oauthService = manager.getService(type);
        return ResponseEntity.ok(new LoginPageResponse(oauthService.createLoginUrl()));
    }

    @GetMapping("/{type}/callback")
    @Operation(hidden = true)
    public ResponseEntity<OauthResponse> callback(
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
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "access token 재발급 API", description = "refresh 토큰을 Header로 받아 새로운 access token을 발급받습니다.")
    public ResponseEntity<TokenResponse> refresh(
            HttpServletRequest request
    ) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken = token.substring(JwtProvider.JWT_PREFIX.length());
        return ResponseEntity.ok(tokenService.createRefreshToken(refreshToken));
    }
}
