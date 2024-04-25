package com.codeit.blob.comment.repository;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPostOrderByCreatedDateAsc(Post post, Pageable pageable);

}
