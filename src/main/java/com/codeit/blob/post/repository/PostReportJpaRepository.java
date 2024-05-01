package com.codeit.blob.post.repository;

import com.codeit.blob.post.domain.PostReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostReportJpaRepository extends JpaRepository<PostReport, Long> {

    Optional<PostReport> findByUserIdAndPostId(Long userId, Long postId);
}
