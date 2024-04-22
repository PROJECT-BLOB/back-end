package com.codeit.blob.oauth.controller;

import com.codeit.blob.oauth.OauthType;
import com.codeit.blob.oauth.service.OauthManager;
import com.codeit.blob.oauth.service.OauthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Tag(name = "Oauth API", description = "Oauth 로그인 API")
public class OauthController {

    private final OauthManager manager;

    @GetMapping("/{type}")
    public ResponseEntity<String> createLoginPage(
            @PathVariable("type") String oauthType
    ) {
        OauthType type = OauthType.valueOf(oauthType.toUpperCase());
        OauthService oauthService = manager.getService(type);
        return ResponseEntity.ok(oauthService.createLoginUrl());
    }

    @GetMapping("/{type}/callback")
    @Operation(hidden = true)
    public ResponseEntity<Object> callback(
            @PathVariable(name = "type") String oauthType,
            @RequestParam(name = "code") String code
    ) {
        OauthType type = OauthType.valueOf(oauthType.toUpperCase());
        OauthService oauthService = manager.getService(type);
        return ResponseEntity.ok(oauthService.createToken(code));
    }
}
