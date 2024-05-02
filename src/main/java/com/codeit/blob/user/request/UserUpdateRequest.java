package com.codeit.blob.user.request;

import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Schema(name = "유저 정보 수정 요청 데이터")
public class UserUpdateRequest {
    @NotEmpty(message = "nickName 는 필수 데이터 입니다.")
    @Schema(description = "유저 닉네임", example = "코드코드")
    private final String nickName;

    @Schema(description = "유저 페이지 한줄 설명", example = "안녕하세요. 여행을 좋아하는 블로비라고 합니다. 좋은 정보를 공유합니다. 즐겁게 여행해요")
    private final String bio;

    @Schema(description = "프로필 공개 / 비공개")
    private final Boolean isPrivate;

    @Schema(description = "상세 위치 위도", example = "37.532600")
    private final Double lat;

    @Schema(description = "상세 위치 경도", example = "127.024612")
    private final Double lng;


    public UserUpdateRequest(String nickName, String bio, boolean isPrivate, Double lat, Double lng) {
        if(bio.length() > 50){
            throw new CustomException(ErrorCode.BAD_ENUM_REQUEST);
        }

        this.nickName = nickName;
        this.bio = bio;
        this.isPrivate = isPrivate;
        this.lat = lat;
        this.lng = lng;
    }
}
