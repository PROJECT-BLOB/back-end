package com.codeit.blob.post.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(name = "게시글 작성 요청 데이터")
public class CreatePostRequest {

    @NotEmpty(message = "게시글 제목은 필수값입니다.")
    @Schema(description = "게시글 제목", example = "title")
    private String title;

    @Size(max = 2000)
    @Schema(description = "게시글 내용, 최대 2000자", example = "content")
    private String content;

    @NotNull(message = "게시글 카테고리는 필수값입니다.")
    @Schema(description = "게시글 카테고리", example = "QUESTION")
    private String category;

    @Schema(description = "게시글 세부 카테고리", example = "WEATHER")
    private String subcategory;

    @NotEmpty(message = "나라 정보는 필수값입니다.")
    @Schema(description = "나라 정보", example = "대한민국")
    private String country;

    @NotEmpty(message = "도시 정보는 필수값입니다.")
    @Schema(description = "도시 정보", example = "서울")
    private String city;

    @NotNull(message = "도시 정보는 필수값입니다.")
    @Schema(description = "도시 기준 위도", example = "37.532600")
    private Double cityLat;

    @NotNull(message = "도시 정보는 필수값입니다.")
    @Schema(description = "도시 기준 경도", example = "127.024612")
    private Double cityLng;

    @Schema(description = "상세 위치 위도", example = "37.532600")
    private Double lat;

    @Schema(description = "상세 위치 경도", example = "127.024612")
    private Double lng;

    @Schema(description = "상세 위치 주소", example = "서울특별시 영등포구 의사당대로 1")
    private String address;

    @Schema(description = "유저의 실제 위치 위도", example = "37.532600")
    private Double actualLat;

    @Schema(description = "유저의 실제 위치 경도", example = "127.024612")
    private Double actualLng;

}
