package com.codeit.blob.post.dto.response;

import com.codeit.blob.post.domain.Post;
import com.codeit.blob.post.domain.PostImage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(name = "게시글 상세보기 응답")
public class PostResponse {

    private final Long postId;
    private final String title;
    private final String content;
    private final String category;
    private final String subcategory;
    //private final UserResponse author;
    private final String country;
    private final String city;
    private final Double lat;
    private final Double lng;
    private final Long distFromActual;
    private final Long views;
    private final List<String> imageUrl;

    public PostResponse(Post post){
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory().getLabel();
        this.subcategory = post.getSubcategory().getLabel();
        // user
        this.country = post.getCity().getCountry().getLabel();
        this.city = post.getCity().getName();
        this.lat = post.getCoordinate().getLat();
        this.lng = post.getCoordinate().getLng();
        this.distFromActual = post.getDistFromActual();
        this.views = getViews();
        this.imageUrl = post.getPostImages().stream().map(PostImage::getUrl).toList();
    }
}
