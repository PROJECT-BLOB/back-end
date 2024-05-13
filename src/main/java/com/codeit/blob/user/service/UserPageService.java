package com.codeit.blob.user.service;

import com.codeit.blob.comment.repository.CommentRepositoryImpl;
import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;
import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.post.domain.Bookmark;
import com.codeit.blob.post.domain.Post;
import com.codeit.blob.post.repository.BookmarkJpaRepository;
import com.codeit.blob.post.repository.PostJpaRepository;
import com.codeit.blob.post.response.PostPageResponse;
import com.codeit.blob.user.UserState;
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

    public PostPageResponse findUserPosts(CustomUsers userDetails, String blobId, Pageable pageable) {
        Long userId = checkProfileUser(blobId, userDetails);

        Page<Post> postPage = postRepository.findByAuthorId(userId, pageable);
        Users user = userDetails == null ? null : userDetails.getUsers();
        return PostPageResponse.postDetailPageResponse(postPage, user);
    }

    public PostPageResponse findUserComment(CustomUsers userDetails, String blobId, Pageable pageable) {
        Long userId = checkProfileUser(blobId, userDetails);

        Page<Post> postPage = commentRepository.getCommentedPosts(userId, pageable);
        Users user = userDetails == null ? null : userDetails.getUsers();
        return PostPageResponse.postDetailPageResponse(postPage, user);
    }

    public PostPageResponse findUserBookmark(CustomUsers userDetails, String blobId, Pageable pageable) {
        Long userId = checkProfileUser(blobId, userDetails);

        Page<Bookmark> bookmarkPage = bookmarkRepository.findByUserId(userId, pageable);
        Page<Post> postPage = bookmarkPage.map(Bookmark::getPost);
        Users user = userDetails == null ? null : userDetails.getUsers();
        return PostPageResponse.postDetailPageResponse(postPage, user);
    }

    private Long checkProfileUser(String blobId, CustomUsers userDetails){
        Users user = userRepository.findByBlobId(blobId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(user.getState().equals(UserState.DELETED)){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        if (!user.getIsPublic() && (userDetails == null || !userDetails.getUsers().getId().equals(user.getId()))){
            throw new CustomException(ErrorCode.PRIVATE_PROFILE);
        }

        return user.getId();
    }
}
