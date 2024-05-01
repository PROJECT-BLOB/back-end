package com.codeit.blob.post.service;

import com.codeit.blob.city.domain.City;
import com.codeit.blob.city.domain.Country;
import com.codeit.blob.city.service.CityService;
import com.codeit.blob.global.domain.Coordinate;
import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;
import com.codeit.blob.post.domain.*;
import com.codeit.blob.post.repository.*;
import com.codeit.blob.post.request.CreatePostRequest;
import com.codeit.blob.post.request.FeedFilter;
import com.codeit.blob.post.request.MapFilter;
import com.codeit.blob.post.response.*;
import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.user.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final PostRepositoryImpl postRepository;
    private final PostImageJpaRepository imageJpaRepository;
    private final BookmarkJpaRepository bookmarkJpaRepository;
    private final PostLikeJpaRepository postLikeJpaRepository;
    private final PostReportJpaRepository postReportJpaRepository;
    private final CityService cityService;

    @Transactional
    public DetailedPostResponse createPost(
            CustomUsers userDetails,
            CreatePostRequest request,
            List<String> imgPaths
    ) {
        Country country = Country.getInstance(request.getCountry());
        City city = cityService.findCityByCountryAndName(country, request.getCity());
        if (city == null){
            city = cityService.createCity(country, request.getCity());
        }

        Subcategory subcategory = request.getSubcategory() == null ? null : Subcategory.getInstance(request.getSubcategory());

        Coordinate coordinate = request.getLat() == null || request.getLng() == null
                ? null : new Coordinate(request.getLat(), request.getLng());
        Long distFromActual = coordinate == null || request.getActualLat() == null || request.getActualLng() == null
                ? null : coordinate.calculateDistance(request.getActualLat(), request.getActualLng());

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(Category.getInstance(request.getCategory()))
                .subcategory(subcategory)
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

        return new DetailedPostResponse(post, userDetails.getUsers());
    }

    @Transactional
    public DetailedPostResponse viewPost(CustomUsers userDetails, Long postId) {
        Users user = userDetails == null ? null : userDetails.getUsers();
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.incrementView();

        return new DetailedPostResponse(post, user);
    }

    @Transactional
    public String deletePost(CustomUsers userDetails, Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // check if the user deleting the post is the author of the post
        if (!post.getAuthor().getId().equals(userDetails.getUsers().getId())) {
            throw new CustomException(ErrorCode.ACTION_ACCESS_DENIED);
        }

        postJpaRepository.deleteById(postId);
        return "게시글 삭제 성공";
    }

    @Transactional
    public DetailedPostResponse likePost(CustomUsers userDetails, Long postId) {
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

        return new DetailedPostResponse(post, user);
    }

    @Transactional
    public DetailedPostResponse bookmarkPost(CustomUsers userDetails, Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Users user = userDetails.getUsers();

        Bookmark bookmark = bookmarkJpaRepository.findByUserIdAndPostId(user.getId(), postId).orElse(null);

        if (bookmark == null){
            bookmark = new Bookmark(user, post);
            bookmarkJpaRepository.save(bookmark);
            post.addBookmark(bookmark);
        } else {
            bookmarkJpaRepository.deleteById(bookmark.getId());
            post.removeBookmark(bookmark);
        }

        return new DetailedPostResponse(post, user);
    }

    @Transactional(readOnly = true)
    public PostPageResponse getFeed(CustomUsers userDetails, FeedFilter filters) {
        Country country = Country.getInstance(filters.getCountry());
        City city = null;
        if (filters.getCity() != null){
            city = cityService.findCityByCountryAndName(country, filters.getCity());
            if (city == null){
                return new PostPageResponse(Collections.emptyList(), 0, 0, false);
            }
        }

        Users user = userDetails == null ? null : userDetails.getUsers();
        Pageable pageable = PageRequest.of(filters.getPage(), filters.getSize());
        Page<Post> posts = postRepository.getFeed(country, city, filters, pageable);

        return PostPageResponse.postDetailPageResponse(posts, user);
    }

    @Transactional(readOnly = true)
    public List<MapPostResponse> getMap(MapFilter filters) {
        List<Post> posts = postRepository.getMap(filters);
        return posts.stream().map(MapPostResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public PostPageResponse getMapSidebar(MapFilter filters, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.getMapSidebar(filters, pageable, sortBy);
        return PostPageResponse.postMapPageResponse(posts);
    }

    @Transactional
    public String reportPost(CustomUsers userDetails, Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Users user = userDetails.getUsers();

        if (user.getId().equals(post.getAuthor().getId())){
            throw new CustomException(ErrorCode.ACTION_ACCESS_DENIED);
        }

        if (postReportJpaRepository.findByUserIdAndPostId(user.getId(), postId).isPresent()){
            throw new CustomException(ErrorCode.REPORT_ALREADY_EXISTS);
        } else {
            PostReport report = new PostReport(user, post.getAuthor(), post);
            postReportJpaRepository.save(report);
        }

        return "게시글 신고 성공";
    }
}
