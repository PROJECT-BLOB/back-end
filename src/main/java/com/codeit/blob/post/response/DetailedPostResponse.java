package com.codeit.blob.post.response;

import com.codeit.blob.global.converter.DateTimeUtils;
import com.codeit.blob.post.domain.Post;
import com.codeit.blob.post.domain.PostImage;
import com.codeit.blob.user.domain.UserRole;
import com.codeit.blob.user.domain.Users;
import com.codeit.blob.user.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(name = "게시글 상세보기 응답")
public class DetailedPostResponse implements PostResponse {

    private final Long postId;
    private final String title;
    private final String content;
    private final String category;
    private final String subcategory;
    private final UserProfileResponse author;
    private final String country;
    private final String city;
    private final Double lat;
    private final Double lng;
    private final String address;
    private final Long distFromActual;
    private final Long views;
    @Schema(example = "2024-04-24T12:59:24")
    private final String createdDate;
    @Schema(example = "2024-04-24T13:29:24")
    private final String expiresAt;
    private final List<String> imageUrl;
    private final boolean liked;
    private final boolean bookmarked;
    private final int likeCount;
    private final int commentCount;
    private final boolean canDelete;

    public DetailedPostResponse(Post post, Users user){
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory().name();
        this.subcategory = post.getSubcategory() == null ? null : post.getSubcategory().name();
        this.author = post.getAuthor() == null ? null : UserProfileResponse.of(post.getAuthor());
        this.country = post.getCity().getCountry().getLabel();
        this.city = post.getCity().getName();
        this.lat = post.getCoordinate() == null ? null : post.getCoordinate().getLat();
        this.lng = post.getCoordinate() == null ? null : post.getCoordinate().getLng();
        this.address = post.getAddress();
        this.distFromActual = post.getDistFromActual();
        this.views = post.getViews();
        this.createdDate = DateTimeUtils.format(post.getCreatedDate());
        this.expiresAt = DateTimeUtils.format(post.getExpiresAt());
        this.imageUrl = post.getPostImages().stream().map(PostImage::getUrl).toList();
        this.liked = user != null && post.getLikes().stream().map(l -> l.getUser().getId()).toList().contains(user.getId());
        this.bookmarked = user != null && post.getBookmarks().stream().map(b -> b.getUser().getId()).toList().contains(user.getId());
        this.likeCount = postId%2 == 0 ? 100 + postId.intValue() : post.getLikes().size();
        this.commentCount = post.getComments().size();
        this.canDelete = user != null && (user.getId().equals(post.getAuthor().getId()) || user.getRole().equals(UserRole.ROLE_ADMIN));
    }
}
