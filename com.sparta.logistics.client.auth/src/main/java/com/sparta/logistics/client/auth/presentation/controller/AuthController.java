package com.sparta.logistics.client.auth.presentation.controller;


import com.sparta.logistics.client.auth.application.service.AuthService;
import com.sparta.logistics.client.auth.application.util.JWTUtil;
import com.sparta.logistics.client.auth.domain.model.UserVO;
import com.sparta.logistics.client.auth.presentation.request.SignInRequest;
import com.sparta.logistics.client.auth.presentation.request.UserRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JWTUtil jwtUtil;

    @PostMapping("/auth/signup")
    public ResponseEntity<String> signUp(
            @RequestBody UserRequest request, HttpServletResponse response) {
        UserVO userVO = authService.signUp(request);

        log.info("sign-up is successfully :: {}", userVO.getUsername());

        return ResponseEntity.ok().body("signUp successfully");
    }
    @PostMapping("/auth/signin")
    public ResponseEntity<AuthResponse> createAuthenticationToken(
            @RequestBody SignInRequest request, HttpServletResponse response) {

        log.info("request :: {}", request);

        UserVO userVO = authService.signIn(request,response);

        return ResponseEntity.ok(new AuthResponse(userVO));
    }
    /**
     * JWT 액세스 토큰을 포함하는 응답 객체입니다.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class AuthResponse {
        private UserVO user;

    }
}