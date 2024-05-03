package com.codeit.blob.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(name = "유저 추가 인증 요청 데이터")
public class UserRequest {

    @NotEmpty(message = "blobId 는 필수 데이터 입니다.")
    @Schema(description = "유저 서비스 아이디", example = "blobblob1234")
    private final String blobId;

    @NotEmpty(message = "nickName 는 필수 데이터 입니다.")
    @Schema(description = "닉네임", example = "코드코드")
    private final String nickName;

}
