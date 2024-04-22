package com.codeit.blob.user.service;

import com.codeit.blob.user.domain.Users;
import com.codeit.blob.user.repository.UserRepository;
import com.codeit.blob.user.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Users join(UserRequest userRequest) {
        Users users = userRepository.findByOauthId(userRequest.getOauthId())
                .orElseGet(() -> Users.builder()
                        .oauthId(userRequest.getOauthId())
                        .email(userRequest.getEmail())
                        .blobId(userRequest.getBlobId())
                        .nickName(userRequest.getNickName())
                        .profileUrl(userRequest.getProfileUrl())
                        .userAuthenticateType(userRequest.getUserAuthenticateType())
                        .oauthType(userRequest.getOauthType())
                        .build());

        Users save = userRepository.save(users);
        return save;
    }
}
