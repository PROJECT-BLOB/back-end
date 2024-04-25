package com.codeit.blob.global.domain.test;

import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.user.domain.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Test", description = "Test")
public class TestController {

    @GetMapping("/test")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "JWT 테스트 API", description = "JWT 가 제대로 들어오고 인증된지 확인하기")
    public ResponseEntity<Users> requestTest(
            @AuthenticationPrincipal CustomUsers users
    ) {
        return ResponseEntity.ok(users.getUsers());
    }
}
