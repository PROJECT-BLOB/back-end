package com.codeit.blob.post.service;

import com.codeit.blob.city.domain.City;
import com.codeit.blob.city.domain.Country;
import com.codeit.blob.city.service.CityService;
import com.codeit.blob.global.domain.Coordinate;
import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;
import com.codeit.blob.post.domain.*;
import com.codeit.blob.post.repository.PostLikeJpaRepository;
import com.codeit.blob.post.request.CreatePostRequest;
import com.codeit.blob.post.response.DeletePostResponse;
import com.codeit.blob.post.response.PostResponse;
import com.codeit.blob.post.repository.PostImageJpaRepository;
import com.codeit.blob.post.repository.PostJpaRepository;
import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.user.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final PostImageJpaRepository imageJpaRepository;
    private final PostLikeJpaRepository postLikeJpaRepository;
    private final CityService cityService;

    @Transactional
    public PostResponse createPost(
            CustomUsers userDetails,
            CreatePostRequest request,
            List<String> imgPaths
    ) {
        Country country = Country.getInstance(request.getCountry());
        City city = cityService.findCityByCountryAndName(country, request.getCity());
        if (city == null){
            city = cityService.createCity(country, request.getCity());
        }

        Coordinate coordinate = request.getLat() == null || request.getLng() == null
                ? null : new Coordinate(request.getLat(), request.getLng());
        Long distFromActual = coordinate == null || request.getActualLat() == null || request.getActualLng() == null
                ? null : coordinate.calculateDistance(request.getActualLat(), request.getActualLng());

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(Category.getInstance(request.getCategory()))
                .subcategory(Subcategory.getInstance(request.getSubcategory()))
                .author(userDetails.getUsers())
                .city(city)
                .coordinate(coordinate)
                .distFromActual(distFromActual)
                .build();

        postJpaRepository.save(post);
        for (String imgUrl : imgPaths) {
            PostImage img = new PostImage(imgUrl, post);
            imageJpaRepository.save(img);
            post.addImage(img);
        }

        return new PostResponse(post, userDetails.getUsers());
    }

    @Transactional
    public PostResponse viewPost(CustomUsers userDetails, Long postId) {
        Users user = userDetails == null ? null : userDetails.getUsers();
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.incrementView();

        return new PostResponse(post, user);
    }

    @Transactional
    public DeletePostResponse deletePost(CustomUsers userDetails, Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // check if the user deleting the post is the author of the post
        if (!post.getAuthor().getId().equals(userDetails.getUsers().getId())) {
            throw new CustomException(ErrorCode.ACTION_ACCESS_DENIED);
        }

        postJpaRepository.deleteById(postId);
        return new DeletePostResponse(postId);
    }

    @Transactional
    public PostResponse likePost(CustomUsers userDetails, Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Users user = userDetails.getUsers();

        PostLike like = postLikeJpaRepository.findByUserIdAndPostId(user.getId(), postId).orElse(null);

        if (like == null){
            // add like if post was not previously liked
            like = new PostLike(user, post);
            postLikeJpaRepository.save(like);
            post.addLike(like);
        } else {
            // delete like if post was previously liked
            postLikeJpaRepository.deleteById(like.getId());
            post.removeLike(like);
        }

        return new PostResponse(post, user);
    }
}
