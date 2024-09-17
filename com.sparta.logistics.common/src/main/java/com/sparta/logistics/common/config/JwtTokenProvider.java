package com.sparta.logistics.common.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class JwtTokenProvider {
    @Value("${service.jwt.secret-key}")
    private String jwtSecret;  // 실제 JWT 서명 키
    // JWT에서 user_id 추출
    public Long getUserIdFromToken(String token) {
        byte[] bytes = Base64.getDecoder().decode(jwtSecret);
        var secretKey = Keys.hmacShaKeyFor(bytes);
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("user_id", Long.class);  // user_id 추출
    }
}
