package com.codeit.blob.user.service;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.comment.repository.CommentJpaRepository;
import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.post.domain.Bookmark;
import com.codeit.blob.post.domain.Post;
import com.codeit.blob.post.repository.BookmarkJpaRepository;
import com.codeit.blob.post.repository.PostJpaRepository;
import com.codeit.blob.user.response.UserPostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileService {

    private final PostJpaRepository postRepository;

    /**
     * 내가 쓴 글 조회
     */
    public List<UserPostResponse> findUserPosts(CustomUsers users, Pageable pageable) {
        Page<Post> postByPage = postRepository.findPostByAuthor(users.getUsers(), pageable);
        List<UserPostResponse> collect = postByPage.getContent().stream()
                .map(post -> new UserPostResponse(post))
                .collect(Collectors.toList());
        return collect;
    }


}
