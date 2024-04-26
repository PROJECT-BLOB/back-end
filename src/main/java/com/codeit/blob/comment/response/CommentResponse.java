package com.codeit.blob.comment.response;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.user.domain.Users;
import com.codeit.blob.user.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Schema(name = "댓글 정보 응답")
public class CommentResponse {
    private final Long id;
    private final String content;
    private final UserProfileResponse author;
    @Schema(example = "2024-04-24T12:59:24")
    private final String createdDate;
    private final boolean liked;
    private final int likeCount;
    private final boolean canDelete;
    private final List<CommentResponse> reply;

    public CommentResponse(Comment comment, Users user){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = new UserProfileResponse(comment.getAuthor());
        this.createdDate = comment.getCreatedDate().truncatedTo(ChronoUnit.SECONDS).toString();
        this.liked = user != null && comment.getLikes().stream().map(l -> l.getUser().getId()).toList().contains(user.getId());
        this.likeCount = comment.getLikes().size();
        this.canDelete = user != null && comment.getAuthor().getId().equals(user.getId());
        this.reply = comment.getReply().stream().map(r -> new CommentResponse(r, user)).toList();
    }
}
