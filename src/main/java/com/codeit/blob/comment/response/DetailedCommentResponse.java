package com.codeit.blob.comment.response;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.user.domain.UserRole;
import com.codeit.blob.user.domain.Users;
import com.codeit.blob.user.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.temporal.ChronoUnit;

@Getter
@Schema(name = "댓글 정보 응답")
public class DetailedCommentResponse implements CommentResponse {
    private final Long commentId;
    private final Long postId;
    private final String content;
    private final UserProfileResponse author;
    @Schema(example = "2024-04-24T12:59:24")
    private final String createdDate;
    private final boolean liked;
    private final int likeCount;
    private final int replyCount;
    private final boolean canDelete;

    public DetailedCommentResponse(Comment comment, Users user){
        this.commentId = comment.getId();
        this.postId = comment.getPost().getId();
        this.content = comment.getContent();
        this.author = new UserProfileResponse(comment.getAuthor());
        this.createdDate = comment.getCreatedDate().truncatedTo(ChronoUnit.SECONDS).toString();
        this.liked = user != null && comment.getLikes().stream().map(l -> l.getUser().getId()).toList().contains(user.getId());
        this.likeCount = comment.getLikes().size();
        this.replyCount = comment.getReply().size();
        this.canDelete = user != null && (comment.getAuthor().getId().equals(user.getId()) || user.getRole().equals(UserRole.ROLE_ADMIN));
    }
}
