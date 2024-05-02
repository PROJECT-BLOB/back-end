package com.codeit.blob.comment.response;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.user.domain.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(name = "댓글 페이지 응답")
public class CommentPageResponse {

    private final List<? extends CommentResponse> content;
    private final long count;
    private final long currentPage;
    private final boolean hasMore;
    private final long remaining;

    public static CommentPageResponse commentDetailedPageResponse(Page<Comment> page, Users user){
        return new CommentPageResponse(
                page.getContent().stream().map(c -> new DetailedCommentResponse(c, user)).toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.hasNext(),
                Math.max(0, page.getTotalElements() - (page.getNumber() + 1L) * page.getSize())
        );
    }

    public static CommentPageResponse commentReportedPageResponse(Page<Comment> page){
        return new CommentPageResponse(
                page.getContent().stream().map(ReportedCommentResponse::new).toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.hasNext(),
                Math.max(0, page.getTotalElements() - (page.getNumber() + 1L) * page.getSize())
        );
    }
}
