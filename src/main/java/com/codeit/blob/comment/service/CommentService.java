package com.codeit.blob.comment.service;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.comment.domain.CommentLike;
import com.codeit.blob.comment.domain.CommentReport;
import com.codeit.blob.comment.repository.CommentJpaRepository;
import com.codeit.blob.comment.repository.CommentLikeJpaRepository;
import com.codeit.blob.comment.repository.CommentReportJpaRepository;
import com.codeit.blob.comment.request.CreateCommentRequest;
import com.codeit.blob.comment.response.CommentPageResponse;
import com.codeit.blob.comment.response.CommentResponse;
import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;
import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.post.domain.Post;
import com.codeit.blob.post.repository.PostJpaRepository;
import com.codeit.blob.user.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentJpaRepository commentJpaRepository;
    private final CommentLikeJpaRepository likeJpaRepository;
    private final CommentReportJpaRepository reportJpaRepository;
    private final PostJpaRepository postJpaRepository;

    @Transactional
    public CommentResponse createComment(
            CustomUsers userDetails,
            Long postId,
            CreateCommentRequest request
    ) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(userDetails.getUsers())
                .post(post)
                .build();

        commentJpaRepository.save(comment);
        return new CommentResponse(comment, userDetails.getUsers());
    }

    @Transactional
    public CommentResponse createReply(
            CustomUsers userDetails,
            Long parentId,
            CreateCommentRequest request
    ) {
        Comment parent = commentJpaRepository.findById(parentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // make max reply depth 1
        if (parent.getParent() != null){
            parent = parent.getParent();
        }

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(userDetails.getUsers())
                .parent(parent)
                .post(parent.getPost())
                .build();

        commentJpaRepository.save(comment);
        return new CommentResponse(comment, userDetails.getUsers());
    }

    @Transactional
    public String deleteComment(
            CustomUsers userDetails,
            Long commentId
    ) {
        Comment comment = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // check if the user deleting the comment is the author of the comment
        if (!comment.getAuthor().getId().equals(userDetails.getUsers().getId())) {
            throw new CustomException(ErrorCode.ACTION_ACCESS_DENIED);
        }

        commentJpaRepository.deleteById(commentId);
        return "댓글 삭제 성공";
    }

    @Transactional(readOnly = true)
    public CommentPageResponse getPostComments(
            CustomUsers userDetails,
            Long postId,
            int page,
            int size
    ){
        Users user = userDetails == null ? null : userDetails.getUsers();
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentJpaRepository.findByPostAndParentOrderByCreatedDateAsc(post, null, pageable);

        return new CommentPageResponse(comments, user);
    }

    @Transactional(readOnly = true)
    public CommentPageResponse getCommentReplies(
            CustomUsers userDetails,
            Long commentId,
            int page,
            int size
    ){
        Users user = userDetails == null ? null : userDetails.getUsers();
        Comment parent = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentJpaRepository.findByParentIdOrderByCreatedDateAsc(parent.getId(), pageable);

        return new CommentPageResponse(comments, user);
    }

    @Transactional
    public CommentResponse likeComment(CustomUsers userDetails, Long commentId) {
        Comment comment = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        Users user = userDetails.getUsers();

        CommentLike like = likeJpaRepository.findByUserIdAndCommentId(user.getId(), commentId).orElse(null);

        if (like == null){
            like = new CommentLike(user, comment);
            likeJpaRepository.save(like);
            comment.addLike(like);
        } else {
            likeJpaRepository.deleteById(like.getId());
            comment.removeLike(like);
        }

        return new CommentResponse(comment, user);
    }

    @Transactional
    public String reportComment(CustomUsers userDetails, Long commentId) {
        Comment comment = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        Users user = userDetails.getUsers();

        if (user.getId().equals(comment.getAuthor().getId())){
            throw new CustomException(ErrorCode.ACTION_ACCESS_DENIED);
        }

        if (reportJpaRepository.findByReporterIdAndCommentId(user.getId(), commentId).isPresent()){
            throw new CustomException(ErrorCode.REPORT_ALREADY_EXISTS);
        } else {
            CommentReport report = new CommentReport(user, comment.getAuthor(), comment);
            reportJpaRepository.save(report);
        }

        return "댓글 신고 성공";
    }
}
