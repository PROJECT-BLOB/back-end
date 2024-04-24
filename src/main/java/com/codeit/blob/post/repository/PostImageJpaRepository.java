package com.codeit.blob.post.repository;

import com.codeit.blob.post.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageJpaRepository extends JpaRepository<PostImage, Long> {
}
