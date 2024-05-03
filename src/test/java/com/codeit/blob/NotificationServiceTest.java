package com.codeit.blob;

import com.codeit.blob.comment.request.CreateCommentRequest;
import com.codeit.blob.comment.service.CommentService;
import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.notification.response.NotificationPageResponse;
import com.codeit.blob.notification.service.NotificationService;
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

import java.util.Collections;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("알림 서비스 테스트")
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    private Users[] users;
    private CustomUsers[] userDetails;
    private CreatePostRequest postRequest;
    private CreateCommentRequest commentRequest;

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

        postRequest = new CreatePostRequest("title", "content", "HELP", "WEATHER", "대한민국", "서울", 37.532600, 127.024612, 37.532600, 127.024612);
        commentRequest = new CreateCommentRequest("content");
    }

    @Test
    @DisplayName("알림 생성 및 조회")
    void getNotifications() {
        //given
        postService.createPost(userDetails[0], postRequest, Collections.emptyList());

        //when
        postService.likePost(userDetails[0], 1L);
        postService.likePost(userDetails[1], 1L);
        commentService.createComment(userDetails[0], 1L, commentRequest);
        commentService.createComment(userDetails[1], 1L, commentRequest);
        commentService.createReply(userDetails[0], 2L, commentRequest);
        commentService.createReply(userDetails[1], 2L, commentRequest);
        commentService.createReply(userDetails[1], 3L, commentRequest);

        NotificationPageResponse response1 = notificationService.getNotifications(userDetails[0], 0, 10);
        NotificationPageResponse response2 = notificationService.getNotifications(userDetails[1], 0, 10);

        //then
        Assertions.assertNotNull(response1);
        Assertions.assertEquals(2, response1.getCount());
        Assertions.assertEquals(2L, response1.getContent().get(0).getNotificationId());
        Assertions.assertEquals(1L, response1.getContent().get(0).getPostId());

        Assertions.assertNotNull(response2);
        Assertions.assertEquals(1, response2.getCount());
        Assertions.assertEquals(3L, response2.getContent().get(0).getNotificationId());
    }

    @Test
    @DisplayName("알림 삭제")
    void deleteNotification() {
        //given
        postService.createPost(userDetails[0], postRequest, Collections.emptyList());
        postService.likePost(userDetails[1], 1L);

        //when
        String response1 = notificationService.readNotification(userDetails[0], 1L);
        NotificationPageResponse response2 = notificationService.getNotifications(userDetails[0], 0, 10);

        //then
        Assertions.assertNotNull(response1);
        Assertions.assertNotNull(response2);
        Assertions.assertEquals(0, response2.getCount());
    }

    @Test
    @DisplayName("알림 삭제 실패")
    void deleteNotificationFail() {
        //given
        postService.createPost(userDetails[0], postRequest, Collections.emptyList());
        postService.likePost(userDetails[1], 1L);

        //then
        Assertions.assertThrows(CustomException.class, () -> notificationService.readNotification(userDetails[0], 2L));
        Assertions.assertThrows(CustomException.class, () -> notificationService.readNotification(userDetails[1], 1L));
    }

    @Test
    @DisplayName("알림 모두 읽기")
    void readAllNotifications() {
        //given
        postService.createPost(userDetails[0], postRequest, Collections.emptyList());
        postService.likePost(userDetails[0], 1L);
        postService.likePost(userDetails[1], 1L);
        commentService.createComment(userDetails[0], 1L, commentRequest);
        commentService.createComment(userDetails[1], 1L, commentRequest);
        commentService.createReply(userDetails[0], 2L, commentRequest);
        commentService.createReply(userDetails[1], 2L, commentRequest);
        commentService.createReply(userDetails[1], 3L, commentRequest);

        //when
        notificationService.readAllNotifications(userDetails[0]);
        NotificationPageResponse response1 = notificationService.getNotifications(userDetails[0], 0, 10);
        NotificationPageResponse response2 = notificationService.getNotifications(userDetails[1], 0, 10);

        //then
        Assertions.assertNotNull(response1);
        Assertions.assertEquals(0, response1.getCount());

        Assertions.assertNotNull(response2);
        Assertions.assertEquals(1, response2.getCount());
    }




}
