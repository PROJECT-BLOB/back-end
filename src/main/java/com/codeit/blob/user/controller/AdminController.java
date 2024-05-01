package com.codeit.blob.user.controller;

import com.codeit.blob.comment.response.CommentPageResponse;
import com.codeit.blob.comment.service.CommentService;
import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.post.response.PostPageResponse;
import com.codeit.blob.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "관리자 API", description = "관리자/신고 조회 관련 API")
public class AdminController {

    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/post")
    @Operation(summary = "신고된 게시글 조회 API", description = "신고된 게시글들을 조회합니다.")
    public ResponseEntity<PostPageResponse> getReportedPosts(
            @RequestParam(defaultValue = "10") int min,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(postService.getReportedPosts(min, page, size));
    }

    @GetMapping("/comment")
    @Operation(summary = "신고된 댓글 조회 API", description = "신고된 댓글들을 조회합니다.")
    public ResponseEntity<CommentPageResponse> getReportedComments(
            @RequestParam(defaultValue = "10") int min,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(commentService.getReportedComments(min, page, size));
    }


}
