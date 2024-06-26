package com.codeit.blob.global.config;

import com.codeit.blob.jwt.exception.JwtExceptionHandler;
import com.codeit.blob.jwt.filter.JwtAuthenticationFilter;
import com.codeit.blob.jwt.filter.UserAuthenticationFilter;
import com.codeit.blob.jwt.provider.JwtProvider;
import com.codeit.blob.user.repository.UserRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class SecurityConfig {

    private static final String[] PERMIT_URL = {
            "/v3/**", "/swagger-ui/**", "/oauth/**", "/user/**", "/post/**", "/comment/**"
    };

    @Bean
    @Profile("default")
    public SecurityFilterChain defaultConfig(HttpSecurity http, JwtProvider provider, UserRepository userRepository) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);

        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http.authorizeHttpRequests(request -> request
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .requestMatchers(PERMIT_URL).permitAll()
                .requestMatchers(antMatcher("/admin/**")).hasRole("ADMIN")
                .anyRequest().authenticated());

        http
                .addFilterBefore(new JwtAuthenticationFilter(provider, userRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionHandler(), JwtAuthenticationFilter.class)
                .addFilterAfter(new UserAuthenticationFilter(), JwtAuthenticationFilter.class);

        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    @Profile("dev")
    public SecurityFilterChain devConfig(HttpSecurity http, JwtProvider provider, UserRepository userRepository) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("http://localhost:3000", "https://blob-omega.vercel.app"));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setExposedHeaders(List.of("*"));
            config.setAllowCredentials(true);
            return config;
        }));

        http.authorizeHttpRequests(request -> request
                .requestMatchers(PERMIT_URL).permitAll()
                .requestMatchers(antMatcher("/admin/**")).hasRole("ADMIN")
                .anyRequest().authenticated());

        http
                .addFilterBefore(new JwtAuthenticationFilter(provider, userRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionHandler(), JwtAuthenticationFilter.class)
                .addFilterAfter(new UserAuthenticationFilter(), JwtAuthenticationFilter.class);

        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
