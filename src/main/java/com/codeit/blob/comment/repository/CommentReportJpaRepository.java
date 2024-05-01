package com.codeit.blob.comment.repository;

import com.codeit.blob.comment.domain.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentReportJpaRepository extends JpaRepository<CommentReport, Long> {

    Optional<CommentReport> findByUserIdAndPostId(Long userId, Long postId);
}
