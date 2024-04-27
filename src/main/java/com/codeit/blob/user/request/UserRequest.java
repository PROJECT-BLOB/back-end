package com.codeit.blob.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
@Schema(name = "유저 추가 인증 요청 데이터")
public class UserRequest {
    @NotEmpty(message = "oauthId 는 필수 데이터 입니다.")
    @Schema(description = "Oauth 아이디", example = "14685162935")
    private final String oauthId;

    @NotEmpty(message = "blobId 는 필수 데이터 입니다.")
    @Schema(description = "유저 서비스 아이디", example = "blobblob1234")
    private final String blobId;

    @NotEmpty(message = "nickName 는 필수 데이터 입니다.")
    @Schema(description = "닉네임", example = "코드코드")
    private final String nickName;

    public UserRequest(String oauthId, String blobId, String nickName) {
        this.oauthId = oauthId;
        this.blobId = blobId;
        this.nickName = nickName;
    }
}
