package com.codeit.blob.user.service;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.comment.repository.CommentJpaRepository;
import com.codeit.blob.comment.response.CommentPageResponse;
import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;
import com.codeit.blob.post.domain.Bookmark;
import com.codeit.blob.post.domain.Post;
import com.codeit.blob.post.repository.BookmarkJpaRepository;
import com.codeit.blob.post.repository.PostJpaRepository;
import com.codeit.blob.post.response.PostPageResponse;
import com.codeit.blob.user.domain.Users;
import com.codeit.blob.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPageService {

    private final PostJpaRepository postRepository;
    private final CommentJpaRepository commentRepository;
    private final BookmarkJpaRepository bookmarkRepository;
    private final UserRepository userRepository;

    public PostPageResponse findUserPosts(Long userId, Pageable pageable) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Page<Post> postPage = postRepository.findByAuthorId(userId, pageable);
        return PostPageResponse.postDetailPageResponse(postPage, users);
    }

    public CommentPageResponse findUserComment(Long userId, Pageable pageable) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Page<Comment> byAuthor = commentRepository.findByAuthorId(userId, pageable);
        return CommentPageResponse.commentDetailedPageResponse(byAuthor, users);
    }

    public PostPageResponse findUserBookmark(Long userId, Pageable pageable) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Page<Bookmark> bookmarkPage = bookmarkRepository.findByUserId(userId, pageable);
        Page<Post> postPage = bookmarkPage.map(Bookmark::getPost);
        return PostPageResponse.postDetailPageResponse(postPage, users);
    }
}
