package com.codeit.blob;

import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.post.request.CreatePostRequest;
import com.codeit.blob.post.response.DeletePostResponse;
import com.codeit.blob.post.response.PostResponse;
import com.codeit.blob.post.service.PostService;
import com.codeit.blob.user.UserAuthenticateState;
import com.codeit.blob.user.domain.Users;
import com.codeit.blob.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("게시글 서비스 테스트")
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    private Users[] users;
    private CustomUsers[] userDetails;
    private CreatePostRequest request;
    private List<String> images;

    @BeforeEach
    public void setup(){
        users = new Users[2];
        userDetails = new CustomUsers[2];
        for (int i = 0; i < 2; i++) {
            Users user = Users.builder()
                    .blobId("blobId" + i)
                    .nickName("nickname" + i)
                    .state(UserAuthenticateState.COMPLETE)
                    .build();
            users[i] = user;
            userRepository.save(user);
            userDetails[i] = new CustomUsers(user);
        }

        request = new CreatePostRequest("title", "content", "HELP", "WEATHER", "대한민국", "서울", 37.532600, 127.024612, 37.532600, 127.024612);
        images = List.of("image.com");
    }

    @Test
    @DisplayName("게시글 작성 성공")
    void createPost() {
        //when
        PostResponse response = postService.createPost(userDetails[0], request, images);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(request.getTitle(), response.getTitle());
        Assertions.assertEquals(request.getContent(), response.getContent());
        Assertions.assertEquals(users[0].getBlobId(), response.getAuthor().getBlobId());
    }

    @Test
    @DisplayName("작성자가 게시글 조회")
    void viewPostAuthor() {
        //given
        postService.createPost(userDetails[0], request, images);

        //when
        PostResponse response = postService.viewPost(userDetails[0], 1L);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(request.getTitle(), response.getTitle());
        Assertions.assertEquals(request.getContent(), response.getContent());
        Assertions.assertEquals(1, response.getViews());
        Assertions.assertTrue(response.isCanDelete());
    }

    @Test
    @DisplayName("다른 유저가 게시글 조회")
    void viewPostUser() {
        //given
        postService.createPost(userDetails[0], request, images);

        //when
        PostResponse response = postService.viewPost(userDetails[1], 1L);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(request.getTitle(), response.getTitle());
        Assertions.assertEquals(request.getContent(), response.getContent());
        Assertions.assertFalse(response.isCanDelete());
    }

    @Test
    @DisplayName("로그인을 하지 않고 게시글 조회")
    void viewPostGuest() {
        //given
        postService.createPost(userDetails[0], request, images);

        //when
        PostResponse response = postService.viewPost(null, 1L);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(request.getTitle(), response.getTitle());
        Assertions.assertEquals(request.getContent(), response.getContent());
        Assertions.assertFalse(response.isCanDelete());
    }

    @Test
    @DisplayName("작성자가 게시글 삭제")
    void deletePostByAuthor() {
        //given
        postService.createPost(userDetails[0], request, images);

        //when
        DeletePostResponse response = postService.deletePost(userDetails[0], 1L);

        //then
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("다른 유저는 게시글 삭제 실패")
    void deletePostFail() {
        //given
        postService.createPost(userDetails[0], request, images);

        //then
        Assertions.assertThrows(CustomException.class, () -> postService.deletePost(userDetails[1], 1L));
    }

    @Test
    @DisplayName("게시글 좋아요")
    void likePost() {
        //given
        postService.createPost(userDetails[0], request, images);

        //when
        postService.likePost(userDetails[0], 1L);
        PostResponse response = postService.likePost(userDetails[1], 1L);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.getLikeCount());
        Assertions.assertTrue(response.isLiked());
    }

    @Test
    @DisplayName("게시글 좋아요 취소")
    void unlikePost() {
        //given
        postService.createPost(userDetails[0], request, images);

        //when
        postService.likePost(userDetails[0], 1L);
        postService.likePost(userDetails[1], 1L);
        PostResponse response = postService.likePost(userDetails[1], 1L);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getLikeCount());
        Assertions.assertFalse(response.isLiked());
    }

    @Test
    @DisplayName("게시글 저장 및 취소")
    void bookmarkPost() {
        //given
        postService.createPost(userDetails[0], request, images);

        //when
        PostResponse response1 = postService.bookmarkPost(userDetails[0], 1L);
        PostResponse response2 = postService.bookmarkPost(userDetails[0], 1L);

        //then
        Assertions.assertNotNull(response1);
        Assertions.assertTrue(response1.isBookmarked());

        Assertions.assertNotNull(response2);
        Assertions.assertFalse(response2.isBookmarked());
    }
}
