package com.codeit.blob.comment.response;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.user.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.temporal.ChronoUnit;

@Getter
@Schema(name = "신고된 댓글 응답")
public class ReportedCommentResponse implements CommentResponse{

    private final Long commentId;
    private final Long postId;
    private final String content;
    private final UserProfileResponse author;
    @Schema(example = "2024-04-24T12:59:24")
    private final String createdDate;
    private final int likeCount;
    private final int reportCount;

    public ReportedCommentResponse(Comment comment){
        this.commentId = comment.getId();
        this.postId = comment.getPost().getId();
        this.content = comment.getContent();
        this.author = new UserProfileResponse(comment.getAuthor());
        this.createdDate = comment.getCreatedDate().truncatedTo(ChronoUnit.SECONDS).toString();
        this.likeCount = comment.getLikes().size();
        this.reportCount = comment.getReports().size();
    }
}
