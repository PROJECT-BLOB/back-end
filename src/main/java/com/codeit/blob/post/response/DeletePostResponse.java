package com.codeit.blob.post.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "게시글 삭제 성공 응답")
public class DeletePostResponse {

    private final Long postId;
    @Schema(example = "게시물 삭제 성공")
    private final String message;

    public DeletePostResponse(Long postId){
        this.postId = postId;
        this.message = "게시물 삭제 성공";
    }
}
