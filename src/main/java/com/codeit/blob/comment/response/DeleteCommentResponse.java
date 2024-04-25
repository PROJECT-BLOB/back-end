package com.codeit.blob.comment.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "댓글 삭제 응답")
public class DeleteCommentResponse {
    private Long commentId;
    private String message;

    public DeleteCommentResponse(Long commentId){
        this.commentId = commentId;
        this.message = "댓글 삭제 성공";
    }

}
