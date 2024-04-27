package com.codeit.blob.user.controller;

import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.user.request.UserRequest;
import com.codeit.blob.user.request.UserUpdateRequest;
import com.codeit.blob.user.response.UserResponse;
import com.codeit.blob.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User API", description = "회원 CRUD API")
public class UserController {
    private final UserService userService;

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "유저 추가 인증 API", description = "blobId, nickName 을 입력 받아 인증합니다.")
    public ResponseEntity<UserResponse> validationUser(
            @Valid @RequestBody UserRequest userRequest
    ) {
        return ResponseEntity.ok(userService.validationUser(userRequest));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "유저 정보 업데이트 API", description = "nickname, profile 을 입력 받아 업데이트합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(encoding = {
            @Encoding(name = "data", contentType = "application/json"),
            @Encoding(name = "file", contentType = "image/jpg, image/png, image/jpeg")
    }))
    public ResponseEntity<UserResponse> updateUser(
            @AuthenticationPrincipal CustomUsers userDetails,
            @Valid @RequestPart("data") UserUpdateRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {

        UserResponse userResponse = userService.updateUser(request, file, userDetails);
        return ResponseEntity.ok(userResponse);
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
