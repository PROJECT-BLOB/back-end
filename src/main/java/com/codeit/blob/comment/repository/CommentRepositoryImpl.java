package com.codeit.blob.comment.repository;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.post.domain.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.codeit.blob.comment.domain.QComment.comment;
import static com.codeit.blob.comment.domain.QCommentReport.commentReport;


@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Comment> getReportedComments(Pageable pageable, int minReport){
        List<Comment> content = jpaQueryFactory.selectFrom(comment)
                .distinct()
                .leftJoin(comment.reports, commentReport)
                .where(comment.reports.size().goe(minReport))
                .orderBy(comment.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory.select(comment.count()).from(comment)
                .distinct()
                .leftJoin(comment.reports, commentReport)
                .where(comment.reports.size().goe(minReport))
                .fetchOne();
        total = total == null ? 0 : total;

        return new PageImpl<>(content, pageable, total);
    }

    public Page<Post> getCommentedPosts(Long userId, Pageable pageable){
        List<Post> content = jpaQueryFactory.select(comment.post)
                .distinct()
                .from(comment)
                .where(comment.author.id.eq(userId))
                .orderBy(comment.post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory.select(comment.post.count())
                .distinct()
                .from(comment)
                .where(comment.author.id.eq(userId))
                .fetchOne();
        total = total == null ? 0 : total;

        return new PageImpl<>(content, pageable, total);
    }

}
