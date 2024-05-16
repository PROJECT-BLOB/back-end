package com.codeit.blob.jwt.provider;

import com.codeit.blob.user.domain.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtProvider {
    public static final String JWT_PREFIX = "Bearer ";

    @Value("${oauth2.jwt.secret-key}")
    private String secretKey;
    private static final long ACCESS_EXPIRE_DATE = 1000L * 60 * 30;
    private static final long REFRESH_EXPIRE_DATE = 1000L * 60 * 60 * 24 * 7;

    public String generateAccessToken(Map<String, Object> extractClaims) {
        return Jwts.builder()
                .setClaims(extractClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRE_DATE))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Map<String, Object> extractClaims) {
        return Jwts.builder()
                .setClaims(extractClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRE_DATE))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, Users user) {
        final String oauthId = extractClaim(token, claims -> claims.get("oauthId", String.class));
        return (oauthId.equals(user.getOauthId())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration)
                .before(new Date());
    }


    private Key getSignKey() {
        byte[] decode = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(decode);
    }
}
