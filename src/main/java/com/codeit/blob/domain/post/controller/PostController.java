package com.codeit.blob.domain.post.controller;

import com.codeit.blob.domain.post.dto.request.CreatePostRequest;
import com.codeit.blob.domain.post.dto.response.DeletePostResponse;
import com.codeit.blob.domain.post.dto.response.PostResponse;
import com.codeit.blob.domain.post.service.PostService;
import com.codeit.blob.oauth.domain.CustomUsers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "게시글 API", description = "게시글 관련 API 목록입니다.")
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping
    @Operation(summary = "게시글 작성")
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal CustomUsers userDetails,
            @Valid @RequestPart("data") CreatePostRequest request,
            @RequestPart("file") List<MultipartFile> files
    ) {
        //todo 사진 업로드
        return ResponseEntity.ok(postService.createPost(userDetails, request));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 조회")
    public ResponseEntity<PostResponse> viewPost(
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(postService.viewPost(postId));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제")
    public ResponseEntity<DeletePostResponse> deletePost(
            @AuthenticationPrincipal CustomUsers userDetails,
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(postService.deletePost(userDetails, postId));
    }
}
