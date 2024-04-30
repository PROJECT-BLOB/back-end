package com.codeit.blob.jwt.filter;

import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;
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
            "/oauth", "/h2-console", "/swagger-ui", "/v3"
    };

    private static final String[] GET_EXCLUDE_PATH = {
            "/post/", "/comment/", "/user"
    };

    private final JwtProvider provider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("--[Request URL : {}]", request.getRequestURL());

        final String jwtPrefix = JwtProvider.JWT_PREFIX;
        final String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("--[Request Jwt Token : {}]", jwtToken);

        if (jwtToken == null || !jwtToken.startsWith(jwtPrefix)) {
            log.info("---[Access Token Not Validation]---");
            throw new CustomException(ErrorCode.JWT_VALIDATED_FAIL);
        }

        try {
            String accessToken = jwtToken.substring(jwtPrefix.length());
            if (!provider.isTokenExpired(accessToken)) {
                log.info("---[Access Token Not Expired]---");

                String oauthId = provider.extractClaim(accessToken, claims -> String.valueOf(claims.get("oauthId")));
                Users users = userRepository.findByOauthId(oauthId)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

                if (users.getRefreshToken().isEmpty()) {
                    throw new CustomException(ErrorCode.JWT_EXPIRED);
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
            throw new CustomException(ErrorCode.JWT_EXPIRED);
        } catch (SignatureException e) {
            log.info("---[Access Token Not Validation]---");
            throw new CustomException(ErrorCode.JWT_VALIDATED_FAIL);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // check if token is null so that signed-in users can get extra features
        final String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        boolean getExclude = jwtToken == null
                && "GET".equals(method)
                && Arrays.stream(GET_EXCLUDE_PATH).anyMatch(path::startsWith);

        return Arrays.stream(EXCLUDE_PATH).anyMatch(path::startsWith) || getExclude;
    }

}
