package com.sparta.logistics.client.order.common.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CustomAuditorAware implements AuditorAware<Long> {

    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();

    // 사용자 ID를 설정하는 메서드
    public void setCurrentUser(String userId) {
        currentUser.set(userId);
        log.info("Current user set to: {}", userId);
    }

    @Override
    public Optional<Long> getCurrentAuditor() {
        String userId = currentUser.get();
        if (userId != null) {
            try {
                return Optional.of(Long.valueOf(userId));
            } catch (NumberFormatException e) {
                // 변환 실패 시 로깅 또는 기본값 처리
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    // 사용자 ID 초기화
    public void clear() {
        currentUser.remove();
    }
}
