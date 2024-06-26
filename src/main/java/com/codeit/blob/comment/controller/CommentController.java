package com.codeit.blob.comment.controller;

import com.codeit.blob.comment.request.CreateCommentRequest;
import com.codeit.blob.comment.response.CommentPageResponse;
import com.codeit.blob.comment.response.DetailedCommentResponse;
import com.codeit.blob.comment.service.CommentService;
import com.codeit.blob.oauth.domain.CustomUsers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<DetailedCommentResponse> createComment(
            @AuthenticationPrincipal CustomUsers userDetails,
            @PathVariable Long postId,
            @RequestBody @Valid CreateCommentRequest request
    ) {
        return ResponseEntity.ok(commentService.createComment(userDetails, postId, request));
    }

    @PostMapping("/reply/{commentId}")
    @Operation(summary = "답글 작성 API", description = "commentId와 댓글 내용을 받아 해당 댓글에 답글을 작성합니다.")
    public ResponseEntity<DetailedCommentResponse> createReply(
            @AuthenticationPrincipal CustomUsers userDetails,
            @PathVariable Long commentId,
            @RequestBody @Valid CreateCommentRequest request
    ) {
        return ResponseEntity.ok(commentService.createReply(userDetails, commentId, request));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제 API", description = "commentId를 받아 유저가 작성자일 경우 해당 댓글/답글을 삭제합니다.")
    public ResponseEntity<String> deleteComment(
            @AuthenticationPrincipal CustomUsers userDetails,
            @PathVariable Long commentId
    ) {
        return ResponseEntity.ok(commentService.deleteComment(userDetails, commentId));
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "댓글 조회 API", description = "postId를 받아 해당 게시글의 댓글을 오래된순으로 조회합니다. (토큰 필수 X)")
    public ResponseEntity<CommentPageResponse> getPostComments(
            @AuthenticationPrincipal CustomUsers userDetails,
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(commentService.getPostComments(userDetails, postId, page, size));
    }

    @GetMapping("/reply/{commentId}")
    @Operation(summary = "답글 조회 API", description = "commentId를 받아 해당 댓글의 답글을 오래된순으로 조회합니다. (토큰 필수 X)")
    public ResponseEntity<CommentPageResponse> getCommentReplies(
            @AuthenticationPrincipal CustomUsers userDetails,
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(commentService.getCommentReplies(userDetails, commentId, page, size));
    }

    @PostMapping("/like/{commentId}")
    @Operation(summary = "댓글 좋아요/취소 API", description = "commentId를 받아 댓글에 좋아요를 추가하거나 이미 좋아요를 누른 경우 취소합니다.")
    public ResponseEntity<DetailedCommentResponse> likeComment(
            @AuthenticationPrincipal CustomUsers userDetails,
            @PathVariable Long commentId
    ) {
        return ResponseEntity.ok(commentService.likeComment(userDetails, commentId));
    }

    @PostMapping("/report/{commentId}")
    @Operation(summary = "댓글 신고 API", description = "commentId를 받아 유저가 신고한 댓글/답글이 아닐 경우 해당 글을 신고합니다.")
    public ResponseEntity<String> reportComment(
            @AuthenticationPrincipal CustomUsers userDetails,
            @PathVariable Long commentId
    ) {
        return ResponseEntity.ok(commentService.reportComment(userDetails, commentId));
    }
}
