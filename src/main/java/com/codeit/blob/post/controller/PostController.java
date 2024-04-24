package com.codeit.blob.post.controller;

import com.codeit.blob.global.s3.S3Service;
import com.codeit.blob.post.dto.request.CreatePostRequest;
import com.codeit.blob.post.dto.response.DeletePostResponse;
import com.codeit.blob.post.dto.response.PostResponse;
import com.codeit.blob.post.service.PostService;
import com.codeit.blob.oauth.domain.CustomUsers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "게시글 API", description = "게시글 관련 API 목록입니다.")
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final S3Service s3Service;

    @PostMapping("/new")
    @Operation(summary = "게시글 작성")
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal CustomUsers userDetails,
            @Valid @RequestPart("data") CreatePostRequest request,
            @RequestPart(value = "file", required = false) List<MultipartFile> files
    ) {
        List<String> imgPaths = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            imgPaths = s3Service.upload(files);
        }

        return ResponseEntity.ok(postService.createPost(userDetails, request, imgPaths));
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
