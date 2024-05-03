package com.codeit.blob.comment.repository;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPostAndParentOrderByCreatedDateAsc(Post post, Comment parent, Pageable pageable);

    Page<Comment> findByParentIdOrderByCreatedDateAsc(Long parentId, Pageable pageable);

    Page<Comment> findByAuthorId(Long userId, Pageable pageable);
}
