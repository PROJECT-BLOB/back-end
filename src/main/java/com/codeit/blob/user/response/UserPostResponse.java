package com.codeit.blob.user.response;

import com.codeit.blob.city.domain.Country;
import com.codeit.blob.global.domain.Coordinate;
import com.codeit.blob.post.domain.Category;
import com.codeit.blob.post.domain.Post;
import com.codeit.blob.post.domain.Subcategory;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserPostResponse {

    private final Long id;
    private final String title;
    private final String content;

    private final Category category;
    private final Subcategory subcategory;
    private final Coordinate coordinate;

    private final Long distFromActual;
    private final Long view;

    private final CityResponse city;
    private final UserProfileResponse user;

    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public UserPostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.subcategory = post.getSubcategory();
        this.coordinate = post.getCoordinate();
        this.distFromActual = post.getDistFromActual();
        this.view = post.getViews();

        this.city = new CityResponse(post.getCity().getCountry(), post.getCity().getName());
        this.user = new UserProfileResponse(post.getAuthor());

        this.createdDate = post.getCreatedDate();
        this.modifiedDate = post.getModifiedDate();
    }

    private record CityResponse(Country country, String name) {
    }
}
