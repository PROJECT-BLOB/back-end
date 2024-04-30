package com.codeit.blob.post.repository;

import com.codeit.blob.post.domain.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkJpaRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByUserIdAndPostId(Long userId, Long postId);

    List<Bookmark> findByUserIdAndCityId(Long userId, Long cityId);

    @Query("select b from Bookmark b join fetch b.post where b.user.blobId = :blobId")
    Page<Bookmark> findByUserBlobId(@Param("blobId") String blobId, Pageable pageable);

}
