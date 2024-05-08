package com.codeit.blob.user.service;

import com.codeit.blob.comment.repository.CommentJpaRepository;
import com.codeit.blob.comment.repository.CommentRepositoryImpl;
import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;
import com.codeit.blob.oauth.domain.CustomUsers;
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
    private final CommentRepositoryImpl commentRepository;
    private final BookmarkJpaRepository bookmarkRepository;
    private final UserRepository userRepository;

    public PostPageResponse findUserPosts(CustomUsers userDetails, Long userId, Pageable pageable) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (users.getIsPrivate() && (userDetails == null || !userDetails.getUsers().getId().equals(userId))){
            throw new CustomException(ErrorCode.PRIVATE_PROFILE);
        }

        Page<Post> postPage = postRepository.findByAuthorId(userId, pageable);
        return PostPageResponse.postDetailPageResponse(postPage, users);
    }

    public PostPageResponse findUserComment(CustomUsers userDetails, Long userId, Pageable pageable) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (users.getIsPrivate() && (userDetails == null || !userDetails.getUsers().getId().equals(userId))){
            throw new CustomException(ErrorCode.PRIVATE_PROFILE);
        }

        Page<Post> postPage = commentRepository.getCommentedPosts(userId, pageable);
        return PostPageResponse.postDetailPageResponse(postPage, users);
    }

    public PostPageResponse findUserBookmark(CustomUsers userDetails, Long userId, Pageable pageable) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (users.getIsPrivate() && (userDetails == null || !userDetails.getUsers().getId().equals(userId))){
            throw new CustomException(ErrorCode.PRIVATE_PROFILE);
        }

        Page<Bookmark> bookmarkPage = bookmarkRepository.findByUserId(userId, pageable);
        Page<Post> postPage = bookmarkPage.map(Bookmark::getPost);
        return PostPageResponse.postDetailPageResponse(postPage, users);
    }
}
