package com.codeit.blob.jwt.filter;

import com.codeit.blob.jwt.exception.JwtExpiredException;
import com.codeit.blob.jwt.exception.JwtValidationException;
import com.codeit.blob.jwt.exception.UserNotValidationException;
import com.codeit.blob.jwt.provider.JwtProvider;
import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.user.domain.Users;
import com.codeit.blob.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String[] EXCLUDE_PATH = {
            "/oauth", "/user", "/h2-console", "/swagger-ui", "/v3"
    };

    private final JwtProvider provider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("--[Request URL : {}]", request.getRequestURL());

        final String jwtPrefix = JwtProvider.JWT_PREFIX;
        final String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("--[Request Jwt Token : {}]", jwtToken);

        //access token validate
        if (jwtToken == null || !jwtToken.startsWith(jwtPrefix)) {
            log.info("---[Access Token Not Validation]---");
            throw new JwtValidationException();
        }

        try {
            String accessToken = jwtToken.substring(jwtPrefix.length() - 1);
            if (!provider.isTokenExpired(accessToken)) {
                log.info("---[Access Token Not Expired]---");

                String oauthId = provider.extractClaim(accessToken, claims -> String.valueOf(claims.get("oauthId")));
                Users users = userRepository.findByOauthId(oauthId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));


                if (users.getBlobId() == null || users.getNickName() == null) {
                    throw new UserNotValidationException();
                }

                CustomUsers userDetail = new CustomUsers(users);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetail, userDetail.getPassword(), userDetail.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("---[Jwt Validate Success]---");
            }

        } catch (ExpiredJwtException e) {
            log.error("---[Access Token Expired]---");
            throw new JwtExpiredException();
        } catch (SignatureException e) {
            log.info("---[Access Token Not Validation]---");
            throw new JwtValidationException();
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.stream(EXCLUDE_PATH)
                .anyMatch(path::startsWith);
    }
}
