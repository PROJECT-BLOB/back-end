package com.codeit.blob.comment.repository;

import com.codeit.blob.comment.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeJpaRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByUserIdAndCommentId(Long userId, Long commentId);

}
