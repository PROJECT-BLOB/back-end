package com.codeit.blob.comment.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(name = "댓글 작성 요청 DTO")
public class CreateCommentRequest {

    @NotEmpty(message = "댓글 내용은 필수값입니다.")
    @Schema(description = "댓글 내용", example = "content")
    private String content;

}
