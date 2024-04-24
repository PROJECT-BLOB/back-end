package com.codeit.blob.post.service;

import com.codeit.blob.city.domain.City;
import com.codeit.blob.city.domain.Country;
import com.codeit.blob.city.service.CityService;
import com.codeit.blob.post.domain.Post;
import com.codeit.blob.post.domain.PostImage;
import com.codeit.blob.post.domain.Category;
import com.codeit.blob.post.domain.Subcategory;
import com.codeit.blob.post.dto.request.CreatePostRequest;
import com.codeit.blob.post.dto.response.DeletePostResponse;
import com.codeit.blob.post.dto.response.PostResponse;
import com.codeit.blob.post.repository.PostImageJpaRepository;
import com.codeit.blob.post.repository.PostJpaRepository;
import com.codeit.blob.oauth.domain.CustomUsers;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final PostImageJpaRepository imageJpaRepository;
    private final CityService cityService;

    @Transactional
    public PostResponse createPost(CustomUsers userDetails, CreatePostRequest request, List<String> imgPaths) {

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(request.getLng(), request.getLat()));
        Point actualPoint = geometryFactory.createPoint(new Coordinate(request.getActualLng(), request.getActualLat()));

        Country country = Country.getInstance(request.getCountry());
        City city = cityService.findCityByCountryAndName(country, request.getCity());
        if (city == null){
            city = cityService.createCity(country, request.getCity());
        }

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(Category.getInstance(request.getCategory()))
                .subcategory(Subcategory.getInstance(request.getSubcategory()))
                .author(null) //TODO
                .city(city)
                .location(point)
                .actualLocation(actualPoint)
                .build();

        for (String imgUrl : imgPaths) {
            PostImage img = new PostImage(imgUrl);
            post.addImage(img);
            imageJpaRepository.save(img);
        }

        postJpaRepository.save(post);
        return new PostResponse(post);
    }

    @Transactional(readOnly = true)
    public PostResponse viewPost(Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(IllegalArgumentException::new);
        post.incrementView();

        return new PostResponse(post);
    }

    @Transactional
    public DeletePostResponse deletePost(CustomUsers userDetails, Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(IllegalArgumentException::new);

        // check if the user deleting the post is the author of the post
        if (!post.getAuthor().getId().equals(userDetails.getUsers().getId())) {
            throw new IllegalArgumentException();
        }

        postJpaRepository.deleteById(postId);
        return new DeletePostResponse(postId, "게시물 삭제 성공");
    }
}
