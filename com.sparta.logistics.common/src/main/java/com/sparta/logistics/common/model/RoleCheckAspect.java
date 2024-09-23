package com.sparta.logistics.common.model;


import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;

@Aspect
@Component
public class RoleCheckAspect {

    @Around("@annotation(roleCheck)")  // @RoleCheck 애노테이션이 있는 메서드를 대상으로
    public Object checkRole(ProceedingJoinPoint joinPoint, RoleCheck roleCheck) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // 헤더에서 역할 정보 추출
        String userRole = request.getHeader("X-User-Roles");
        System.out.println("User Role in Header: " + userRole); // 역할 정보 확인용 로그

        // 헤더에 역할이 존재하고, 그것이 애노테이션에서 요구하는 역할과 일치하는지 확인
        if (userRole != null && Arrays.asList(roleCheck.roles()).contains(userRole)) {
            return joinPoint.proceed(); // 역할이 맞으면 메서드 실행
        } else {
            throw new AccessDeniedException("Access denied. Required role: " + Arrays.toString(roleCheck.roles()));
        }
    }
}

