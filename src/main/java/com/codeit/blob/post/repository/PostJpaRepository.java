package com.codeit.blob.post.repository;

import com.codeit.blob.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Long> {

    Page<Post> findByAuthorId(Long userId, Pageable pageable);
}
