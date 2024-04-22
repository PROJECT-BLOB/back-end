package com.codeit.blob.account.service;

import com.codeit.blob.account.domain.Users;
import com.codeit.blob.account.repository.AccountRepository;
import com.codeit.blob.account.request.AccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional
    public Users join(AccountRequest accountRequest) {
        Users users = accountRepository.findByOauthId(accountRequest.getOauthId())
                .orElseGet(() -> Users.builder()
                        .oauthId(accountRequest.getOauthId())
                        .email(accountRequest.getEmail())
                        .blobId(accountRequest.getBlobId())
                        .nickName(accountRequest.getNickName())
                        .profileUrl(accountRequest.getProfileUrl())
                        .userAuthenticateType(accountRequest.getUserAuthenticateType())
                        .oauthType(accountRequest.getOauthType())
                        .build());

        Users save = accountRepository.save(users);
        return save;
    }
}
