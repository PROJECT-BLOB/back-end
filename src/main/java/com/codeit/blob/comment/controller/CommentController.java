package com.codeit.blob.comment.controller;

import com.codeit.blob.comment.request.CreateCommentRequest;
import com.codeit.blob.comment.response.CommentResponse;
import com.codeit.blob.comment.response.DeleteCommentResponse;
import com.codeit.blob.comment.service.CommentService;
import com.codeit.blob.oauth.domain.CustomUsers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "댓글 API", description = "댓글 관련 API 목록입니다.")
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}")
    @Operation(summary = "댓글 작성 API", description = "postId와 댓글 내용을 받아 해당 게시글에 댓글을 작성합니다.")
    public ResponseEntity<CommentResponse> createComment(
            @AuthenticationPrincipal CustomUsers userDetails,
            @PathVariable Long postId,
            @RequestBody @Valid CreateCommentRequest request
    ) {
        return ResponseEntity.ok(commentService.createComment(userDetails, postId, request));
    }

    @PostMapping("/reply/{commentId}")
    @Operation(summary = "답글 작성 API", description = "commentId와 댓글 내용을 받아 해당 댓글에 답글을 작성합니다.")
    public ResponseEntity<CommentResponse> createReply(
            @AuthenticationPrincipal CustomUsers userDetails,
            @PathVariable Long commentId,
            @RequestBody @Valid CreateCommentRequest request
    ) {
        return ResponseEntity.ok(commentService.createReply(userDetails, commentId, request));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제 API", description = "commentId를 받아 유저가 작성자일 경우 해당 댓글/답글을 삭제합니다.")
    public ResponseEntity<DeleteCommentResponse> deleteComment(
            @AuthenticationPrincipal CustomUsers userDetails,
            @PathVariable Long commentId
    ) {
        return ResponseEntity.ok(commentService.deleteComment(userDetails, commentId));
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "댓글 조회 API", description = "postId를 받아 해당 게시글의 댓글을 조회합니다. (토큰 필수 X)")
    public ResponseEntity<Page<CommentResponse>> getPostComments(
            @AuthenticationPrincipal CustomUsers userDetails,
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(commentService.getPostComments(userDetails, postId, page));
    }
}
