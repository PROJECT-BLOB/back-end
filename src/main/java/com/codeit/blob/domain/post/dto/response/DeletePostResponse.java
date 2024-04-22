package com.codeit.blob.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(name = "게시글 상세보기 응답")
public class DeletePostResponse {
    private Long postId;
    private String message;
}
