package com.codeit.blob.comment.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "댓글 삭제 성공 응답")
public class DeleteCommentResponse {

    private final Long commentId;
    private final Long postId;
    @Schema(example = "댓글 삭제 성공")
    private final String message;

    public DeleteCommentResponse(Long commentId, Long postId){
        this.commentId = commentId;
        this.postId = postId;
        this.message = "댓글 삭제 성공";
    }

}
