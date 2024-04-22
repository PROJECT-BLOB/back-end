package com.codeit.blob.domain.post.repository;

import com.codeit.blob.domain.post.domain.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageJpaRepository extends JpaRepository<PostImage, Long> {
}
