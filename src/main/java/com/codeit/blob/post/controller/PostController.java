package com.codeit.blob.post.controller;

import com.codeit.blob.global.s3.S3Service;
import com.codeit.blob.post.request.FeedFilter;
import com.codeit.blob.post.request.CreatePostRequest;
import com.codeit.blob.post.request.MapFilter;
import com.codeit.blob.post.response.*;
import com.codeit.blob.post.service.PostService;
import com.codeit.blob.oauth.domain.CustomUsers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "게시글 API", description = "게시글 관련 API 목록입니다.")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final S3Service s3Service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 작성 API", description = "form-data 형식으로 요청 정보를 data에, 사진을 file에 받아 게시글을 작성합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(encoding = {
            @Encoding(name = "data", contentType = "application/json"),
            @Encoding(name = "file", contentType = "image/jpg, image/png, image/jpeg")
    }))
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal CustomUsers userDetails,
            @Valid @RequestPart("data") CreatePostRequest request,
            @RequestPart(value = "file", required = false) List<MultipartFile> files
    ) {
        List<String> imgPaths = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            if (files.size() > 5) throw new IllegalArgumentException("이미지 최대 5개");
            imgPaths = s3Service.uploadFiles(files);
        }

        return ResponseEntity.ok(postService.createPost(userDetails, request, imgPaths));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 조회 API", description = "postId를 받아 게시글을 조회합니다. (토큰 필수 X)")
    public ResponseEntity<PostResponse> viewPost(
            @AuthenticationPrincipal CustomUsers userDetails,
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(postService.viewPost(userDetails, postId));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제 API", description = "postId를 받아 유저가 작성자일 경우 게시글을 삭제합니다.")
    public ResponseEntity<DeletePostResponse> deletePost(
            @AuthenticationPrincipal CustomUsers userDetails,
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(postService.deletePost(userDetails, postId));
    }

    @PostMapping("/bookmark/{postId}")
    @Operation(summary = "게시글 저장/취소 API", description = "postId를 받아 게시글을 저장하거나 이미 저장한 경우 취소합니다.")
    public ResponseEntity<PostResponse> bookmarkPost(
            @AuthenticationPrincipal CustomUsers userDetails,
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(postService.bookmarkPost(userDetails, postId));
    }

    @PostMapping("/like/{postId}")
    @Operation(summary = "게시글 좋아요/취소 API", description = "postId를 받아 게시글에 좋아요를 추가하거나 이미 좋아요를 누른 경우 취소합니다.")
    public ResponseEntity<PostResponse> likePost(
            @AuthenticationPrincipal CustomUsers userDetails,
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(postService.likePost(userDetails, postId));
    }

    @GetMapping("/feed")
    @Operation(summary = "피드 게시글 조회 API", description = "나라/도시 등 필터링 조건들을 받아 게시글을 조회합니다.")
    public ResponseEntity<PostPageResponse> getFeed(
            @AuthenticationPrincipal CustomUsers userDetails,
            @ModelAttribute FeedFilter filters
    ) {
        return ResponseEntity.ok(postService.getFeed(userDetails, filters));
    }

    @GetMapping("/map")
    @Operation(summary = "지도 게시글 마커 조회 API", description = "필터링 조건들을 받아 지도에 표시할 게시글을 조회합니다.")
    public ResponseEntity<List<PostMapResponse>> getMap(
            @ModelAttribute MapFilter filters
    ) {
        return ResponseEntity.ok(postService.getMap(filters));
    }

    @GetMapping("/map-sidebar")
    @Operation(summary = "지도 사이드바 게시글 조회 API", description = "필터링 조건들을 받아 지도 사이드바에 표시할 게시글을 조회합니다.")
    public ResponseEntity<PostMapPageResponse> getMapSidebar(
            @ModelAttribute MapFilter filters,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "recent") String sortBy
    ) {
        return ResponseEntity.ok(postService.getMapSidebar(filters, page, size, sortBy));
    }
}
