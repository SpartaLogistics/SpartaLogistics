package com.sparta.logistics.client.order.common.filter;

import com.sparta.logistics.client.order.common.domain.CustomAuditorAware;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class UserInfoFilter implements Filter {

    private final CustomAuditorAware auditorAware;

    public UserInfoFilter(CustomAuditorAware auditorAware) {
        this.auditorAware = auditorAware;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String userId = ((HttpServletRequest) request).getHeader("X-User-Id");
            if (userId != null) {
                auditorAware.setCurrentUser(userId);
            }
            chain.doFilter(request, response);
        } finally {
            auditorAware.clear(); // 요청 후 사용자 ID 제거
        }
    }


}