package com.codeit.blob.user.service;

import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;
import com.codeit.blob.global.s3.S3Service;
import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.post.domain.Coordinate;
import com.codeit.blob.user.UserAuthenticateState;
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

    @Transactional
    public UserResponse validationUser(UserRequest userRequest) {
        Users users = userRepository.findByOauthId(userRequest.getOauthId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        users.changeUser(
                users.toBuilder()
                        .blobId(userRequest.getBlobId())
                        .nickName(userRequest.getNickName())
                        .state(UserAuthenticateState.NORMAL)
                        .build()
        );
        userRepository.save(users);

        return new UserResponse(users);
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
                        .nickName(userRequest.getNickName())
                        .coordinate(new Coordinate(userRequest.getLat(), userRequest.getLng()))
                        .profileUrl(profileUrl)
                        .build()
        );

        users = userRepository.save(users);
        return new UserResponse(users);
    }

    public UserResponse findByOauthId(String oauthId) {
        Users users = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 아이디 입니다."));

        return new UserResponse(users);
    }

    public UserResponse findByBlobId(String blobId) {
        Users users = userRepository.findByBlobId(blobId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 아이디 입니다."));

        return new UserResponse(users);
    }
}
