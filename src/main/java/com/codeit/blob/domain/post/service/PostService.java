package com.codeit.blob.domain.post.service;

import com.codeit.blob.domain.post.domain.entity.Post;
import com.codeit.blob.domain.post.domain.enums.Category;
import com.codeit.blob.domain.post.domain.enums.Subcategory;
import com.codeit.blob.domain.post.dto.request.CreatePostRequest;
import com.codeit.blob.domain.post.dto.response.DeletePostResponse;
import com.codeit.blob.domain.post.dto.response.PostResponse;
import com.codeit.blob.domain.post.repository.PostJpaRepository;
import com.codeit.blob.oauth.domain.CustomUsers;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postJpaRepository;

    @Transactional
    public PostResponse createPost(CustomUsers userDetails, CreatePostRequest request) {

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(request.getLng(), request.getLat()));
        Point actualPoint = geometryFactory.createPoint(new Coordinate(request.getActualLng(), request.getActualLat()));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(Category.getInstance(request.getCategory()))
                .subcategory(Subcategory.getInstance(request.getSubcategory()))
                .author(userDetails.getUsers())
                .city(null) // TODO
                .location(point)
                .actualLocation(actualPoint)
                .build();

        postJpaRepository.save(post);
        return new PostResponse(post);
    }

    @Transactional(readOnly = true)
    public PostResponse viewPost(Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(IllegalArgumentException::new);

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
