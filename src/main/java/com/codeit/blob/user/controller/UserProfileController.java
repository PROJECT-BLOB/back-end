package com.codeit.blob.user.controller;

import com.codeit.blob.comment.response.CommentResponse;
import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.user.response.UserPostResponse;
import com.codeit.blob.user.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserProfileController {
    private final UserProfileService service;

    @GetMapping("/profile")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "유저가 작성한 게시글 조회 API", description = "Page 처리를 통한 게시글 조회")
    public ResponseEntity<List<UserPostResponse>> findPostPage(
            @AuthenticationPrincipal CustomUsers users,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.findUserPosts(users, pageable));
    }

    @GetMapping("/comment")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "유저가 작성한 댓글 조회 API", description = "Page 처리를 통한 유저가 작성한 댓글 조회")
    public ResponseEntity<List<CommentResponse>> findComment(
            @AuthenticationPrincipal CustomUsers users,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.findUserComment(users, pageable));
    }
}
