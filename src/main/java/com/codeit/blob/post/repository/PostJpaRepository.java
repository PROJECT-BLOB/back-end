package com.codeit.blob.post.repository;

import com.codeit.blob.post.domain.Post;
import com.codeit.blob.user.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Long> {
    Page<Post> findPostByAuthor(Users users, Pageable pageable);
}
