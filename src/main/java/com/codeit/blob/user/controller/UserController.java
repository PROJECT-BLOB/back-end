package com.codeit.blob.user.controller;

import com.codeit.blob.comment.response.CommentPageResponse;
import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.post.response.PostPageResponse;
import com.codeit.blob.user.request.UserRequest;
import com.codeit.blob.user.request.UserUpdateRequest;
import com.codeit.blob.user.response.UserResponse;
import com.codeit.blob.user.service.UserPageService;
import com.codeit.blob.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User API", description = "유저 관련 API")
public class UserController {
    private final UserService userService;
    private final UserPageService service;

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

    @GetMapping("/{blobId}/profile")
    @Operation(summary = "유저가 작성한 게시글 조회 API", description = "Page 처리를 통한 게시글 조회")
    public ResponseEntity<PostPageResponse> findPostPage(
            @PathVariable("blobId") String blobId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.findUserPosts(blobId, pageable));
    }

    @GetMapping("/{blobId}/comment")
    @Operation(summary = "유저가 작성한 댓글 조회 API", description = "Page 처리를 통한 유저가 작성한 댓글 조회")
    public ResponseEntity<CommentPageResponse> findComment(
            @PathVariable("blobId") String blobId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.findUserComment(blobId, pageable));
    }

    @GetMapping("/{blobId}/bookmark")
    @Operation(summary = "유저의 북마크 조회 API", description = "Page 처리를 통한 북마크 조회")
    public ResponseEntity<PostPageResponse> findBookmark(
            @PathVariable("blobId") String blobId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.findUserBookmark(blobId, pageable));
    }

    @PostMapping("/makeAdmin")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "테스트용 - 관리자 권한 부여 API", description = "유저에게 관리자 권한을 부여합니다.")
    public ResponseEntity<String> makeAdmin(
            @AuthenticationPrincipal CustomUsers userDetails
    ) {
        return ResponseEntity.ok(userService.makeAdmin(userDetails));
    }
}
