package com.sparta.logistics.client.auth.presentation.controller;


import com.sparta.logistics.client.auth.application.service.AuthService;
import com.sparta.logistics.client.auth.application.util.JWTUtil;
import com.sparta.logistics.client.auth.domain.model.UserVO;
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
//    @PostMapping("/auth/signup")
//    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
//        // AuthService에서 회원가입 로직 처리
//        String userId = authService.signUp(signUpRequest);
//
//        // 성공적으로 처리되면 생성된 userId를 반환
//        return ResponseEntity.ok("User registered successfully with ID: " + userId);
//    }

    @PostMapping("/auth/signup")
    public ResponseEntity<String> signUp(
            @RequestBody UserRequest request, HttpServletResponse response) {
        UserVO userVO = authService.signUp(request);

        String accessToken = jwtUtil.createAccessToken(userVO.getId(), userVO.getUsername(), response);

        log.info("sign-up is successfully :: {}", userVO.getUsername());

        return ResponseEntity.ok().body((accessToken));
    }
    /**
     * 사용자 ID를 받아 JWT 액세스 토큰을 생성하여 응답합니다.
     *
     * @param user_id 사용자 ID
     * @return JWT 액세스 토큰을 포함한 AuthResponse 객체를 반환합니다.
     */
//    @GetMapping("/auth/signin")
//    public ResponseEntity<?> createAuthenticationToken(@RequestParam BigInteger user_id, String user_name, HttpServletResponse response){
//        return ResponseEntity.ok(new AuthResponse(jwtUtil.createAccessToken(user_id,user_name,response)));
//    }

//    public ResponseEntity<Void> createAuthenticationToken(
//            @RequestBody SignInRequest request, HttpServletResponse response) {
//
//        log.info("request :: {}", request);
//
//        UserVO userVO = authService.signIn(request);
//
//        jwtUtil.createAccessToken(userVO.getId(), userVO.getUsername(), userVO.getAuthType(), response);
//
//        return ResponseEntity.ok().build();
//    }
    /**
     * JWT 액세스 토큰을 포함하는 응답 객체입니다.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class AuthResponse {
        private String access_token;

    }
}