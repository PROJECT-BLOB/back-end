package com.codeit.blob.user.service;

import com.codeit.blob.global.domain.Coordinate;
import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;
import com.codeit.blob.global.s3.S3Service;
import com.codeit.blob.notification.repository.NotificationJpaRepository;
import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.post.repository.BookmarkJpaRepository;
import com.codeit.blob.user.UserState;
import com.codeit.blob.user.domain.Users;
import com.codeit.blob.user.repository.UserRepository;
import com.codeit.blob.user.request.UserRequest;
import com.codeit.blob.user.request.UserUpdateRequest;
import com.codeit.blob.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final NotificationJpaRepository notificationJpaRepository;
    private final BookmarkJpaRepository bookmarkJpaRepository;

    @Transactional
    public UserResponse validationUser(CustomUsers userDetails, UserRequest userRequest) {
        if (existBlobId(userRequest.getBlobId())) {
            throw new CustomException(ErrorCode.DUPLICATE_BLOB_ID);
        }

        Users users = userDetails.getUsers();

        users.changeUser(
                users.toBuilder()
                        .blobId(userRequest.getBlobId())
                        .nickname(userRequest.getNickname())
                        .bio("안녕하세요. 여행을 좋아하는 블로비라고 합니다. 좋은 정보를 공유합니다. 즐겁게 여행해요")
                        .state(UserState.COMPLETE)
                        .build()
        );

        userRepository.save(users);
        return UserResponse.of(users);
    }

    @Transactional
    public UserResponse updateUser(UserUpdateRequest userRequest, MultipartFile file, CustomUsers userDetails) {
        Users users = userDetails.getUsers();
        String profileUrl = file == null ? users.getProfileUrl() : s3Service.uploadFile(file);

        if (file != null) {
            int i = users.getProfileUrl().lastIndexOf("/");
            String imageName = users.getProfileUrl().substring(i + 1);
            s3Service.deleteFile(imageName);
        }

        users.changeUser(
                users.toBuilder()
                        .nickname(userRequest.getNickname())
                        .coordinate(new Coordinate(userRequest.getLat(), userRequest.getLng()))
                        .isPrivate(userRequest.getIsPrivate())
                        .bio(userRequest.getBio())
                        .profileUrl(profileUrl)
                        .build()
        );

        users = userRepository.save(users);
        return UserResponse.of(users);
    }

    public UserResponse findByUserId(Long userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(users.getState().equals(UserState.DELETED)){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return UserResponse.of(users);
    }

    public boolean existBlobId(String blobId) {
        return userRepository.existsByBlobId(blobId);
    }

    @Transactional
    public String makeAdmin(CustomUsers userDetail) {
        Users user = userDetail.getUsers().makeAdmin();
        userRepository.save(user);
        return "관리자 권한 부여 성공";
    }

    @Transactional
    public String deleteUser(CustomUsers userDetails) {
        Users user = userDetails.getUsers();

        bookmarkJpaRepository.deleteAllByUserId(user.getId());
        notificationJpaRepository.deleteAllByReceiverId(user.getId());
        user.deleteUser();
        userRepository.save(user);
        return "계정 탈퇴 성공";
    }
}
