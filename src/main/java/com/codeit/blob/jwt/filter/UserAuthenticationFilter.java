package com.codeit.blob.jwt.filter;

import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;
import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.user.domain.Users;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class UserAuthenticationFilter extends OncePerRequestFilter {

    private static final String[] GET_EXCLUDE_PATH = {
            ".*/user/[^/]+/check"
    };

    private static final String[] POST_EXCLUDE_PATH = {
            "/user"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }

        CustomUsers details = (CustomUsers) authentication.getPrincipal();
        Users users = details.getUsers();
        if (users.getBlobId() == null || users.getNickname() == null) {
            throw new CustomException(ErrorCode.NEED_MORE_AUTHENTICATE);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        boolean postExclude = "POST".equals(method) && Arrays.stream(POST_EXCLUDE_PATH).anyMatch(path::startsWith);
        boolean getExclude = "GET".equals(method) && Arrays.stream(GET_EXCLUDE_PATH).anyMatch(path::matches);
        return getExclude || postExclude;
    }
}
