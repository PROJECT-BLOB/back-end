package com.codeit.blob.account.controller;

import com.codeit.blob.account.request.AccountRequest;
import com.codeit.blob.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public void createAccount(
            @RequestBody AccountRequest accountRequest
    ) {
        accountService.join(accountRequest);
    }
}
