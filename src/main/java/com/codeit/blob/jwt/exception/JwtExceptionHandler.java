package com.codeit.blob.jwt.exception;

import com.codeit.blob.global.domain.ErrorResponse;
import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Exception Handler
 */
@Slf4j
public class JwtExceptionHandler extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            ErrorCode errorCode = e.getErrorCode();
            ErrorResponse errorResponse = new ErrorResponse(errorCode.getStatus(), errorCode.name(), errorCode.getMessage());
            setResponse(response, errorResponse);
        }
    }

    private static void setResponse(HttpServletResponse response, ErrorResponse errorResponse) {
        response.setStatus(errorResponse.getStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String json = new ObjectMapper().writeValueAsString(errorResponse);
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error("---[Object Mapper Error : {}]---", e.getStackTrace(), e);
        }
    }
}
