package com.codeit.blob.user.service;

import com.codeit.blob.user.UserAuthenticateState;
import com.codeit.blob.user.domain.Users;
import com.codeit.blob.user.repository.UserRepository;
import com.codeit.blob.user.request.UserRequest;
import com.codeit.blob.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponse validationUser(UserRequest userRequest) {
        Users users = userRepository.findByOauthId(userRequest.getOauthId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 아이디 입니다."));

        users.changeUser(
                users.toBuilder()
                        .oauthId(userRequest.getOauthId())
                        .blobId(userRequest.getBlobId())
                        .nickName(userRequest.getNickName())
                        .state(UserAuthenticateState.NORMAL)
                        .build()
        );
        userRepository.save(users);

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

    public UserResponse findByRefreshToken(String refreshToken) {
        Users users = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 아이디 입니다."));

        return new UserResponse(users);
    }
}
