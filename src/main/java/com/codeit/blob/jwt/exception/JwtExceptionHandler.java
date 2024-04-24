package com.codeit.blob.jwt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
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
        } catch (JwtExpiredException e) {
            jwtExpiredExceptionHandler(response);
        } catch (IllegalArgumentException e) {
            illegalArgumentExceptionHandler(response);
        } catch (JwtValidationException e) {
            jwtValidationExceptionHandler(response);
        } catch (UserNotValidationException e) {
            userNotValidationExceptionExceptionHandler(response);
        }
    }

    //Jwt 만료 처리
    private void jwtExpiredExceptionHandler(HttpServletResponse response) {
        log.error("---[Expired Jwt Token]---");
        ErrorResponse errorResponse = new ErrorResponse(JwtStatus.JWT_EXPIRED);
        setResponse(response, errorResponse);
    }

    //Jwt 유효성 검사 실패 처리
    private void jwtValidationExceptionHandler(HttpServletResponse response) {
        log.error("---[Jwt Validation Fail]---");
        ErrorResponse errorResponse = new ErrorResponse(JwtStatus.JWT_VALIDATED_FAIL);
        setResponse(response, errorResponse);
    }

    //인증하지 않은 회원 실패 처리
    private void userNotValidationExceptionExceptionHandler(HttpServletResponse response) {
        log.error("---[User Must Need Validation]---");
        ErrorResponse errorResponse = new ErrorResponse(JwtStatus.USER_NOT_VALIDATED);
        setResponse(response, errorResponse);
    }

    //미처리 에러
    private void illegalArgumentExceptionHandler(HttpServletResponse response) {
        log.error("---[Illegal Argument Exception]---");
        ErrorResponse errorResponse = new ErrorResponse(JwtStatus.ILLEGAL_ARGUMENT_ERROR);
        setResponse(response, errorResponse);
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

    @Getter
    private static class ErrorResponse {
        private final int status;
        private final String message;

        public ErrorResponse(JwtStatus jwtStatus) {
            this.status = jwtStatus.getStatus();
            this.message = jwtStatus.getMessage();
        }
    }
}
