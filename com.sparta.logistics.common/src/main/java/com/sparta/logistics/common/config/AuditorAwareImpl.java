package com.sparta.logistics.common.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {

    private final HttpServletRequest request;
    private final JwtTokenProvider jwtTokenProvider;  // JWT 토큰을 파싱하는 도구

    public AuditorAwareImpl(HttpServletRequest request, JwtTokenProvider jwtTokenProvider) {
        this.request = request;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Optional<Long> getCurrentAuditor() {
        // 요청 헤더에서 JWT 토큰을 추출
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // "Bearer " 제거
            Long userId = jwtTokenProvider.getUserIdFromToken(token);  // JWT에서 user_id 추출
            return Optional.ofNullable(userId);
        }
        return Optional.empty();
    }
}
