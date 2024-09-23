package com.sparta.logistics.common.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {

    private final HttpServletRequest request;

    public AuditorAwareImpl(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Optional<Long> getCurrentAuditor() {
        // 요청 헤더에서 JWT 토큰을 추출
        String userId = request.getHeader("X-User-Id");
        if (userId != null) {
            return Optional.ofNullable(Long.parseLong(userId));
        }
        return Optional.empty();
    }
}
