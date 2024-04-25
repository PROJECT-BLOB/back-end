package com.codeit.blob.comment.service;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.comment.repository.CommentJpaRepository;
import com.codeit.blob.comment.request.CreateCommentRequest;
import com.codeit.blob.comment.response.CommentResponse;
import com.codeit.blob.comment.response.DeleteCommentResponse;
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
    private final PostJpaRepository postJpaRepository;

    @Transactional
    public CommentResponse createComment(
            CustomUsers userDetails,
            Long postId,
            CreateCommentRequest request
    ) {
        if (userDetails == null){
            throw new IllegalArgumentException();
        }
        Users user = userDetails.getUsers();

        Post post = postJpaRepository.findById(postId)
                .orElseThrow(IllegalArgumentException::new);

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(user)
                .post(post)
                .build();

        commentJpaRepository.save(comment);
        return new CommentResponse(comment, user);
    }

    @Transactional
    public CommentResponse createReply(
            CustomUsers userDetails,
            Long parentId,
            CreateCommentRequest request
    ) {
        if (userDetails == null){
            throw new IllegalArgumentException();
        }
        Users user = userDetails.getUsers();

        Comment parent = commentJpaRepository.findById(parentId)
                .orElseThrow(IllegalArgumentException::new);

        // make max reply depth 1
        if (parent.getParent() != null){
            parent = parent.getParent();
        }

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(user)
                .parent(parent)
                .build();

        commentJpaRepository.save(comment);

        return new CommentResponse(parent, user);
    }

    @Transactional
    public DeleteCommentResponse deleteComment(
            CustomUsers userDetails,
            Long commentId
    ) {
        if (userDetails == null){
            throw new IllegalArgumentException();
        }

        Comment comment = commentJpaRepository.findById(commentId)
                .orElseThrow(IllegalArgumentException::new);

        // check if the user deleting the comment is the author of the comment
        if (!comment.getAuthor().getId().equals(userDetails.getUsers().getId())) {
            throw new IllegalArgumentException();
        }

        commentJpaRepository.deleteById(commentId);
        return new DeleteCommentResponse(commentId);
    }

    @Transactional(readOnly = true)
    public Page<CommentResponse> getPostComments(
            CustomUsers userDetails,
            Long postId,
            int page
    ){
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(IllegalArgumentException::new);

        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<Comment> comments = commentJpaRepository.findByPostOrderByCreatedDateAsc(post, pageable);

        return comments.map(c -> new CommentResponse(c, userDetails.getUsers()));
    }
}
