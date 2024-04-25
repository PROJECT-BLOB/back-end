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
    private Long id;
    private String content;
    private UserProfileResponse author;
    private String createdDate;
    private boolean canDelete;
    private List<CommentResponse> reply;

    public CommentResponse(Comment comment, Users user){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = new UserProfileResponse(comment.getAuthor());
        this.createdDate = comment.getCreatedDate().truncatedTo(ChronoUnit.SECONDS).toString();
        this.canDelete = user != null && comment.getAuthor().getId().equals(user.getId());
        this.reply = comment.getReply().stream().map(r -> new CommentResponse(r, user)).toList();
    }
}
