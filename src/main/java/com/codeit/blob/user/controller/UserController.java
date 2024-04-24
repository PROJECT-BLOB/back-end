package com.codeit.blob.user.controller;

import com.codeit.blob.user.request.UserRequest;
import com.codeit.blob.user.response.UserResponse;
import com.codeit.blob.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User API", description = "회원 CRUD API")
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(summary = "회원 인증 API", description = "blobId, nickName 을 입력 받아 인증합니다.")
    public ResponseEntity<UserResponse> validationUser(
            @RequestBody UserRequest userRequest
    ) {
        return ResponseEntity.ok(userService.validationUser(userRequest));
    }

    @GetMapping("/{blobId}/blob")
    @Operation(summary = "회원 조회 API", description = "Blob Id 를 입력받아 회원을 조회합니다.")
    public ResponseEntity<UserResponse> findUserByBlobId(
            @PathVariable("blobId") String blobId
    ) {
        return ResponseEntity.ok(userService.findByBlobId(blobId));
    }

    @GetMapping("/{oauthId}/oauth")
    @Operation(summary = "회원 조회 API", description = "Oauth Id 를 입력받아 회원을 조회합니다.")
    public ResponseEntity<UserResponse> findUserByOauthId(
            @PathVariable("oauthId") String oauthId
    ) {
        return ResponseEntity.ok(userService.findByOauthId(oauthId));
    }
}
