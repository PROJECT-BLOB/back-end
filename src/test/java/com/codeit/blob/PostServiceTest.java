package com.codeit.blob;

import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.post.request.CreatePostRequest;
import com.codeit.blob.post.request.FeedFilter;
import com.codeit.blob.post.request.MapFilter;
import com.codeit.blob.post.response.*;
import com.codeit.blob.post.service.PostService;
import com.codeit.blob.user.UserState;
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

import java.time.LocalDate;
import java.util.Collections;
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
        users = new Users[3];
        userDetails = new CustomUsers[3];
        for (int i = 0; i < 3; i++) {
            Users user = Users.builder()
                    .blobId("blobId" + i)
                    .nickname("nickname" + i)
                    .state(UserState.COMPLETE)
                    .build();
            users[i] = user;
            userRepository.save(user);
            userDetails[i] = new CustomUsers(user);
        }

        request = new CreatePostRequest("title", "content", "HELP", "WEATHER", "대한민국", "서울", 37.532600, 127.024612, 37.532600, 127.024612, "서울특별시 영등포구 의사당대로 1", 37.532600, 127.024612);
        images = List.of("image.com");
    }

    @Test
    @DisplayName("게시글 작성 성공")
    void createPost() {
        //when
        DetailedPostResponse response = postService.createPost(userDetails[0], request, images);

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
        DetailedPostResponse response = postService.viewPost(userDetails[0], 1L);

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
        DetailedPostResponse response = postService.viewPost(userDetails[1], 1L);

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
        DetailedPostResponse response = postService.viewPost(null, 1L);

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
        String response = postService.deletePost(userDetails[0], 1L);

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
        DetailedPostResponse response = postService.likePost(userDetails[1], 1L);

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
        DetailedPostResponse response = postService.likePost(userDetails[1], 1L);

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
        DetailedPostResponse response1 = postService.bookmarkPost(userDetails[0], 1L);
        DetailedPostResponse response2 = postService.bookmarkPost(userDetails[0], 1L);

        //then
        Assertions.assertNotNull(response1);
        Assertions.assertTrue(response1.isBookmarked());

        Assertions.assertNotNull(response2);
        Assertions.assertFalse(response2.isBookmarked());
    }

    @Test
    @DisplayName("피드 조건 검색 - 조회순, 카테고리, 날짜")
    void getFeed1() {
        //given
        CreatePostRequest request2 = new CreatePostRequest("title", "content", "HELP", "ATM", "대한민국", "서울", 37.532600, 127.024612,37.532600, 127.024612, "서울특별시 영등포구 의사당대로 1",37.532600, 127.024612);
        CreatePostRequest request3 = new CreatePostRequest("title", "content", "RECOMMENDED", "HOSPITAL", "대한민국", "전주", 38.532600, 128.024612,37.532600, 127.024612, "서울특별시 영등포구 의사당대로 1",null, null);
        CreatePostRequest request4 = new CreatePostRequest("title", "content", "QUESTION", "WEATHER", "대한민국", "서울", 37.532600, 127.024612,null, null, null, null, null);
        CreatePostRequest request5 = new CreatePostRequest("title", "content", "QUESTION", "TRANSPORT", "대한민국", "서울", 37.532600, 127.024612,37.532600, 127.024612, "서울특별시 영등포구 의사당대로 1",37.532600, 127.024612);

        postService.createPost(userDetails[0], request, images);
        postService.createPost(userDetails[0], request2, images);
        postService.createPost(userDetails[0], request3, Collections.emptyList());
        postService.createPost(userDetails[0], request4, Collections.emptyList());
        postService.createPost(userDetails[0], request5, images);

        postService.viewPost(userDetails[0], 1L);
        postService.viewPost(userDetails[1], 1L);
        postService.viewPost(userDetails[0], 5L);

        FeedFilter filter = new FeedFilter();
        filter.setCityLat(37.532600);
        filter.setCityLng(127.024612);
        filter.setSortBy("views");
        filter.setCategories(List.of("HELP", "NOT_RECOMMENDED", "QUESTION:TRANSPORT"));
        filter.setStartDate(LocalDate.of(2024, 1, 1));
        filter.setEndDate(LocalDate.of(2030, 1, 31));

        //when
        PostPageResponse response = postService.getFeed(userDetails[0], filter);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(3, response.getContent().size());
        Assertions.assertEquals(1L, response.getContent().get(0).getPostId());
        Assertions.assertEquals(5L, response.getContent().get(1).getPostId());
        Assertions.assertEquals(2L, response.getContent().get(2).getPostId());
    }

    @Test
    @DisplayName("피드 조건 검색 - 도시, 좋아요순, 최소 좋아요")
    void getFeed2() {
        //given
        CreatePostRequest request2 = new CreatePostRequest("title", "content", "HELP", "ATM", "대한민국", "서울", 37.532600, 127.024612,37.532600, 127.024612,"서울특별시 영등포구 의사당대로 1", 37.532600, 127.024612);
        CreatePostRequest request3 = new CreatePostRequest("title", "content", "RECOMMENDED", "HOSPITAL", "대한민국", "전주", 38.532600, 128.024612,37.532600, 127.024612, "서울특별시 영등포구 의사당대로 1",null, null);
        CreatePostRequest request4 = new CreatePostRequest("title", "content", "QUESTION", "WEATHER", "대한민국", "서울", 37.532600, 127.024612,null, null, null, null, null);
        CreatePostRequest request5 = new CreatePostRequest("title", "content", "QUESTION", "TRANSPORT", "대한민국", "부산", 36.532600, 126.024612,37.532600, 127.024612, "서울특별시 영등포구 의사당대로 1",37.532600, 127.024612);

        postService.createPost(userDetails[0], request, images);
        postService.createPost(userDetails[0], request2, images);
        postService.createPost(userDetails[0], request3, Collections.emptyList());
        postService.createPost(userDetails[0], request4, Collections.emptyList());
        postService.createPost(userDetails[0], request5, images);
        
        postService.likePost(userDetails[0], 1L);
        postService.likePost(userDetails[1], 1L);
        postService.likePost(userDetails[0], 2L);
        postService.likePost(userDetails[0], 3L);

        FeedFilter filter = new FeedFilter();
        filter.setCityLat(37.532600);
        filter.setCityLng(127.024612);
        filter.setSortBy("likes");
        filter.setMinLikes(1);

        //when
        PostPageResponse response = postService.getFeed(userDetails[0], filter);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.getContent().size());
        Assertions.assertEquals(1L, response.getContent().get(0).getPostId());
        Assertions.assertEquals(2L, response.getContent().get(1).getPostId());
    }

    @Test
    @DisplayName("피드 조건 검색 - 나라, 인기순, 카테고리, 이미지 첨부 여부")
    void getFeed3() {
        //given
        CreatePostRequest request2 = new CreatePostRequest("title", "content", "HELP", "ATM", "대한민국", "서울", 37.532600, 127.024612,37.532600, 127.024612, "서울특별시 영등포구 의사당대로 1",37.532600, 127.024612);
        CreatePostRequest request3 = new CreatePostRequest("title", "content", "RECOMMENDED", "HOSPITAL", "대한민국", "서울", 37.532600, 127.024612,37.532600, 127.024612, "서울특별시 영등포구 의사당대로 1",null, null);
        CreatePostRequest request4 = new CreatePostRequest("title", "content", "QUESTION", "WEATHER", "대한민국", "서울", 37.532600, 127.024612,null, null, null ,null, null);
        CreatePostRequest request5 = new CreatePostRequest("title", "content", "QUESTION", "TRANSPORT", "대한민국", "서울", 37.532600, 127.024612,37.532600, 127.024612, "서울특별시 영등포구 의사당대로 1",37.532600, 127.024612);

        postService.createPost(userDetails[0], request, images);
        postService.createPost(userDetails[0], request2, images);
        postService.createPost(userDetails[0], request3, Collections.emptyList());
        postService.createPost(userDetails[0], request4, Collections.emptyList());
        postService.createPost(userDetails[0], request5, images);

        postService.likePost(userDetails[0], 2L);
        postService.likePost(userDetails[1], 2L);
        postService.likePost(userDetails[0], 3L);


        FeedFilter filter = new FeedFilter();
        filter.setCityLat(37.532600);
        filter.setCityLng(127.024612);
        filter.setCategories(List.of("HELP", "RECOMMENDED", "QUESTION:TRANSPORT", "QUESTION:ATM"));
        filter.setSortBy("hot");
        filter.setHasImage(true);

        //when
        PostPageResponse response = postService.getFeed(userDetails[0], filter);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(3, response.getContent().size());
        Assertions.assertEquals(2L, response.getContent().get(0).getPostId());
        Assertions.assertEquals(5L, response.getContent().get(1).getPostId());
        Assertions.assertEquals(1L, response.getContent().get(2).getPostId());
    }

    @Test
    @DisplayName("피드 조건 검색 - 도시, 최신순, 카테고리, 상세위치 여부")
    void getFeed4() {
        //given
        CreatePostRequest request2 = new CreatePostRequest("title", "content", "HELP", "ATM", "대한민국", "서울", 37.532600, 127.024612,37.532600, 127.024612, "서울특별시 영등포구 의사당대로 1",37.532600, 127.024612);
        CreatePostRequest request3 = new CreatePostRequest("title", "content", "RECOMMENDED", "HOSPITAL", "대한민국", "전주", 38.532600, 128.024612,37.532600, 127.024612, "서울특별시 영등포구 의사당대로 1",null, null);
        CreatePostRequest request4 = new CreatePostRequest("title", "content", "QUESTION", "WEATHER", "대한민국", "서울", 37.532600, 127.024612,null, null, null, null, null);
        CreatePostRequest request5 = new CreatePostRequest("title", "content", "HELP", "TRANSPORT", "대한민국", "서울", 37.532600, 127.024612,null, null, null, null, null);

        postService.createPost(userDetails[0], request, images);
        postService.createPost(userDetails[0], request5, images);
        postService.createPost(userDetails[0], request3, Collections.emptyList());
        postService.createPost(userDetails[0], request4, Collections.emptyList());
        postService.createPost(userDetails[0], request2, images);

        postService.viewPost(userDetails[0], 1L);
        postService.viewPost(userDetails[1], 1L);
        postService.likePost(userDetails[0], 2L);
        postService.likePost(userDetails[1], 2L);
        postService.likePost(userDetails[0], 3L);
        postService.viewPost(userDetails[0], 5L);

        FeedFilter filter = new FeedFilter();
        filter.setCityLat(37.532600);
        filter.setCityLng(127.024612);
        filter.setCategories(List.of("HELP"));
        filter.setHasLocation(true);

        //when
        PostPageResponse response = postService.getFeed(userDetails[0], filter);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.getContent().size());
        Assertions.assertEquals(5L, response.getContent().get(0).getPostId());
        Assertions.assertEquals(1L, response.getContent().get(1).getPostId());
    }

    @Test
    @DisplayName("피드 조건 검색 - 없는 도시")
    void getFeed5() {
        //given
        CreatePostRequest request2 = new CreatePostRequest("title", "content", "HELP", "ATM", "대한민국", "서울", 37.532600, 127.024612,37.532600, 127.024612, "서울특별시 영등포구 의사당대로 1",37.532600, 127.024612);
        CreatePostRequest request3 = new CreatePostRequest("title", "content", "RECOMMENDED", "HOSPITAL", "대한민국", "전주", 38.532600, 128.024612,37.532600, 127.024612, "서울특별시 영등포구 의사당대로 1",null, null);
        
        postService.createPost(userDetails[0], request, images);
        postService.createPost(userDetails[0], request2, images);
        postService.createPost(userDetails[0], request3, Collections.emptyList());

        FeedFilter filter = new FeedFilter();
        filter.setCityLat(1.0);
        filter.setCityLng(1.0);

        //when
        PostPageResponse response = postService.getFeed(userDetails[0], filter);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getContent().size());
    }

    @Test
    @DisplayName("피드 검색 - 키워드 검색")
    void getFeed6() {
        //given
        CreatePostRequest request2 = new CreatePostRequest("title test", "content", "HELP", "ATM", "대한민국", "서울", 37.532600, 127.024612,37.532600, 127.024612, "서울특별시 영등포구 의사당대로 1",37.532600, 127.024612);
        CreatePostRequest request3 = new CreatePostRequest("titletest", "content", "RECOMMENDED", "HOSPITAL", "대한민국", "서울", 37.532600, 127.024612,37.532600, 127.024612, null, null, null);
        CreatePostRequest request4 = new CreatePostRequest("title", "content test", "QUESTION", "WEATHER", "대한민국", "서울", 37.532600, 127.024612,null, null, null,null, null);
        CreatePostRequest request5 = new CreatePostRequest("title", "contest", "QUESTION", "TRANSPORT", "대한민국", "서울", 37.532600, 127.024612,37.532600, 127.024612, "서울특별시 영등포구 의사당대로 1",37.532600, 127.024612);

        postService.createPost(userDetails[0], request, images);
        postService.createPost(userDetails[0], request2, images);
        postService.createPost(userDetails[0], request3, Collections.emptyList());
        postService.createPost(userDetails[0], request4, Collections.emptyList());
        postService.createPost(userDetails[0], request5, images);

        FeedFilter filter = new FeedFilter();
        filter.setCityLat(37.532600);
        filter.setCityLng(127.024612);
        filter.setKeyword("test");

        //when
        PostPageResponse response = postService.getFeed(userDetails[0], filter);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(4, response.getCount());
    }

    @Test
    @DisplayName("지도 조건 검색 - 위치, 카테고리")
    void getMap() {
        //given
        CreatePostRequest request2 = new CreatePostRequest("title", "content", "HELP", "ATM", "대한민국", "서울", 37.532600, 127.024612,150.0, 150.0, "서울특별시 영등포구 의사당대로 1",null, null);
        CreatePostRequest request3 = new CreatePostRequest("title", "content", "RECOMMENDED", "HOSPITAL", "대한민국", "전주", 38.532600, 128.024612,120.0, 120.0, "서울특별시 영등포구 의사당대로 1",null, null);
        CreatePostRequest request4 = new CreatePostRequest("title", "content", "HELP", "WEATHER", "대한민국", "서울", 37.532600, 127.024612,null, null, null, null, null);
        CreatePostRequest request5 = new CreatePostRequest("title", "content", "HELP", null, "대한민국", "서울", 37.532600, 127.024612,130.0, 130.0, "서울특별시 영등포구 의사당대로 1",null, null);

        postService.createPost(userDetails[0], request, images);
        postService.createPost(userDetails[0], request2, images);
        postService.createPost(userDetails[0], request3, Collections.emptyList());
        postService.createPost(userDetails[0], request4, Collections.emptyList());
        postService.createPost(userDetails[0], request5, images);

        MapFilter filter = new MapFilter();
        filter.setCategories(List.of("HELP"));
        filter.setMaxLat(200);
        filter.setMinLat(100);
        filter.setMaxLng(200);
        filter.setMinLng(100);

        //when
        List<MapPostResponse> response = postService.getMap(filter);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.size());
    }

    @Test
    @DisplayName("지도 사이드바 조건 검색 - 위치, 인기순, 카테고리")
    void getMapSidebar() {
        //given
        CreatePostRequest request2 = new CreatePostRequest("title", "content", "HELP", "ATM", "대한민국", "서울", 37.532600, 127.024612,150.0, 150.0, "서울특별시 영등포구 의사당대로 1",null, null);
        CreatePostRequest request3 = new CreatePostRequest("title", "content", "RECOMMENDED", "HOSPITAL", "대한민국", "전주", 38.532600, 128.024612,120.0, 120.0, "서울특별시 영등포구 의사당대로 1",null, null);
        CreatePostRequest request4 = new CreatePostRequest("title", "content", "HELP", "WEATHER", "대한민국", "서울", 37.532600, 127.024612,null, null, null, null, null);
        CreatePostRequest request5 = new CreatePostRequest("title", "content", "HELP", null, "대한민국", "서울", 37.532600, 127.024612,130.0, 130.0, "서울특별시 영등포구 의사당대로 1",null, null);

        postService.createPost(userDetails[0], request, images);
        postService.createPost(userDetails[0], request2, images);
        postService.createPost(userDetails[0], request3, Collections.emptyList());
        postService.createPost(userDetails[0], request4, Collections.emptyList());
        postService.createPost(userDetails[0], request5, images);

        postService.likePost(userDetails[0], 2L);
        postService.likePost(userDetails[1], 2L);
        postService.likePost(userDetails[0], 3L);

        MapFilter filter = new MapFilter();
        filter.setCategories(List.of("HELP", "RECOMMENDED:HOSPITAL", "RECOMMENDED:ATM"));
        filter.setMaxLat(200);
        filter.setMinLat(100);
        filter.setMaxLng(200);
        filter.setMinLng(100);

        //when
        PostPageResponse response = postService.getMapSidebar(filter, 0, 2, "hot");

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(3, response.getContent().size());
        Assertions.assertEquals(2L, response.getContent().get(0).getPostId());
        Assertions.assertEquals(3L, response.getContent().get(1).getPostId());
        Assertions.assertEquals(5L, response.getContent().get(2).getPostId());
    }

    @Test
    @DisplayName("게시글 신고 성공")
    void reportPost() {
        //given
        postService.createPost(userDetails[0], request, images);

        //when
        String response = postService.reportPost(userDetails[1], 1L);

        //then
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("게시글 신고 실패")
    void reportPostFail() {
        //given
        postService.createPost(userDetails[0], request, images);
        postService.reportPost(userDetails[1], 1L);

        //then
        Assertions.assertThrows(CustomException.class, () -> postService.reportPost(userDetails[0], 1L));
        Assertions.assertThrows(CustomException.class, () -> postService.reportPost(userDetails[1], 1L));
    }

    @Test
    @DisplayName("신고된 게시글 조회")
    void getReportedPosts() {
        //given
        postService.createPost(userDetails[0], request, images);
        postService.createPost(userDetails[0], request, images);
        postService.createPost(userDetails[0], request, images);
        postService.reportPost(userDetails[1], 1L);
        postService.reportPost(userDetails[2], 1L);
        postService.reportPost(userDetails[1], 2L);

        //when
        PostPageResponse response = postService.getReportedPosts(2, 0, 10);

        //then
        Assertions.assertEquals(1, response.getCount());
        Assertions.assertEquals(1L, response.getContent().get(0).getPostId());
    }
}
