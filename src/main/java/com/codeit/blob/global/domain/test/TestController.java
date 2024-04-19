package com.codeit.blob.global.domain.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String requestTest(){
        return "요청 성공";
    }
}
