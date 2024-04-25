package com.codeit.blob.post.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "게시글 작성 성공 응답")
public class CreatePostResponse {

    private final Long postId;

    @Schema(example = "게시물 작성 성공")
    private final String message;

    public CreatePostResponse(Long postId){
        this.postId = postId;
        this.message = "게시물 작성 성공";
    }
}
