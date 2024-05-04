package com.codeit.blob;

import com.codeit.blob.comment.request.CreateCommentRequest;
import com.codeit.blob.comment.response.CommentPageResponse;
import com.codeit.blob.comment.response.DetailedCommentResponse;
import com.codeit.blob.comment.service.CommentService;
import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.post.request.CreatePostRequest;
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
@DisplayName("댓글 서비스 테스트")
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    private Users[] users;
    private CustomUsers[] userDetails;
    private CreateCommentRequest request;

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

        CreatePostRequest postRequest = new CreatePostRequest("title", "content", "HELP", "WEATHER", "대한민국", "서울", 37.532600, 127.024612, 37.532600, 127.024612);
        postService.createPost(userDetails[0], postRequest, List.of("image.com"));

        request = new CreateCommentRequest("content");
    }

    @Test
    @DisplayName("새 댓글 작성")
    void createComment() {
        //when
        DetailedCommentResponse response = commentService.createComment(userDetails[0], 1L, request);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.getPostId());
        Assertions.assertEquals(request.getContent(), response.getContent());
    }

    @Test
    @DisplayName("새 답글 작성")
    void createReply() {
        //when
        commentService.createComment(userDetails[0], 1L, request);
        DetailedCommentResponse response = commentService.createReply(userDetails[0], 1L, request);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.getPostId());
        Assertions.assertEquals(request.getContent(), response.getContent());
    }

    @Test
    @DisplayName("본인이 작성한 댓글 삭제")
    void deleteComment() {
        //given
        commentService.createComment(userDetails[0], 1L, request);
        commentService.createReply(userDetails[0], 1L, request);

        //when
        String response1 = commentService.deleteComment(userDetails[0], 1L);
        String response2 = commentService.deleteComment(userDetails[0], 2L);

        //then
        Assertions.assertNotNull(response1);
        Assertions.assertNotNull(response2);
    }

    @Test
    @DisplayName("다른 유저가 작성한 댓글 삭제 실패")
    void deleteCommentFailure() {
        //given
        commentService.createComment(userDetails[0], 1L, request);

        //then
        Assertions.assertThrows(CustomException.class, () -> commentService.deleteComment(userDetails[1], 1L));
    }

    @Test
    @DisplayName("댓글이 있는 게시글 삭제")
    void deletePostWithComments() {
        //given
        commentService.createComment(userDetails[0], 1L, request);
        commentService.createReply(userDetails[0], 1L, request);
        commentService.likeComment(userDetails[0], 1L);

        //when
        String response = postService.deletePost(userDetails[0], 1L);

        //then
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("게시글의 댓글 조회")
    void viewPostComments() {
        //given
        for(int i = 0; i<10; i++){
            commentService.createComment(userDetails[0], 1L, request);
            commentService.createReply(userDetails[0], 1L, request);
        }
        commentService.deleteComment(userDetails[0], 3L);

        //when
        CommentPageResponse response = commentService.getPostComments(userDetails[0], 1L, 0, 5);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(5, response.getContent().size());
        Assertions.assertEquals(9, response.getCount());
        Assertions.assertEquals(0, response.getCurrentPage());
        Assertions.assertEquals(4, response.getRemaining());
        Assertions.assertTrue(response.isHasMore());
    }

    @Test
    @DisplayName("댓글의 답글 조회")
    void viewReplies() {
        //given
        commentService.createComment(userDetails[0], 1L, request);
        for(long i = 1; i<10; i++){
            // also test if max reply depth is 1
            commentService.createReply(userDetails[0], i, request);
        }

        //when
        CommentPageResponse response = commentService.getCommentReplies(userDetails[0], 1L, 0, 5);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(5, response.getContent().size());
        Assertions.assertEquals(9, response.getCount());
        Assertions.assertEquals(0, response.getCurrentPage());
        Assertions.assertEquals(4, response.getRemaining());
        Assertions.assertTrue(response.isHasMore());
    }

    @Test
    @DisplayName("댓글 좋아요")
    void likeComment() {
        //given
        commentService.createComment(userDetails[0], 1L, request);

        //when
        commentService.likeComment(userDetails[0], 1L);
        DetailedCommentResponse response = commentService.likeComment(userDetails[1], 1L);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.getLikeCount());
        Assertions.assertTrue(response.isLiked());
    }

    @Test
    @DisplayName("답글 좋아요 및 취소")
    void unlikeComment() {
        //given
        commentService.createComment(userDetails[0], 1L, request);
        commentService.createReply(userDetails[0], 1L, request);

        //when
        commentService.likeComment(userDetails[0], 2L);
        commentService.likeComment(userDetails[1], 2L);
        DetailedCommentResponse response = commentService.likeComment(userDetails[1], 2L);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getLikeCount());
        Assertions.assertFalse(response.isLiked());
    }

    @Test
    @DisplayName("댓글 신고 성공")
    void reportComment() {
        //given
        commentService.createComment(userDetails[0], 1L, request);
        commentService.createReply(userDetails[0], 1L, request);

        //when
        String response1 = commentService.reportComment(userDetails[1], 1L);
        String response2 = commentService.reportComment(userDetails[1], 2L);

        //then
        Assertions.assertNotNull(response1);
        Assertions.assertNotNull(response2);
    }

    @Test
    @DisplayName("댓글 신고 실패")
    void reportCommentFailure() {
        //given
        commentService.createComment(userDetails[0], 1L, request);
        commentService.createReply(userDetails[0], 1L, request);
        commentService.reportComment(userDetails[1], 1L);
        commentService.reportComment(userDetails[1], 2L);

        //then
        Assertions.assertThrows(CustomException.class, () -> commentService.reportComment(userDetails[0], 1L));
        Assertions.assertThrows(CustomException.class, () -> commentService.reportComment(userDetails[0], 2L));
        Assertions.assertThrows(CustomException.class, () -> commentService.reportComment(userDetails[1], 1L));
        Assertions.assertThrows(CustomException.class, () -> commentService.reportComment(userDetails[1], 2L));
    }

    @Test
    @DisplayName("신고된 댓글 조회")
    void getReportedComments() {
        //given
        commentService.createComment(userDetails[0], 1L, request);
        commentService.createReply(userDetails[0], 1L, request);
        commentService.reportComment(userDetails[1], 2L);

        //when
        CommentPageResponse response = commentService.getReportedComments(1, 0, 10);

        //then
        Assertions.assertEquals(1, response.getCount());
        Assertions.assertEquals(2L, response.getContent().get(0).getCommentId());
    }


}
