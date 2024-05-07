package com.codeit.blob.post.response;

import com.codeit.blob.global.converter.DateTimeUtils;
import com.codeit.blob.post.domain.Post;
import com.codeit.blob.user.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "지도에 표시되는 게시글 응답")
public class MapPostResponse implements PostResponse {

    private final Long postId;
    private final String title;
    private final String category;
    private final String subcategory;
    private final UserProfileResponse author;
    private final Double lat;
    private final Double lng;
    @Schema(example = "2024-04-24T12:59:24")
    private final String createdDate;

    public MapPostResponse(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.category = post.getCategory().name();
        this.subcategory = post.getSubcategory() == null ? null : post.getSubcategory().name();
        this.author = post.getAuthor() == null ? null : new UserProfileResponse(post.getAuthor());
        this.lat = post.getCoordinate().getLat();
        this.lng = post.getCoordinate().getLng();
        this.createdDate = DateTimeUtils.format(post.getCreatedDate());
    }
}
