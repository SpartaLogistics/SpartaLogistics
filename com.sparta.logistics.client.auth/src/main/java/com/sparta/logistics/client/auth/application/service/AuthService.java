package com.sparta.logistics.client.auth.application.service;

import com.sparta.logistics.client.auth.domain.model.UserVO;
import com.sparta.logistics.client.auth.infrastructure.feign.UserClient;
import com.sparta.logistics.client.auth.presentation.request.UserRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserClient userClient;

    public UserVO signUp(UserRequest signUpRequest) {
        // JWT 토큰을 생성하거나 기타 로직을 수행한 후

        // UserRequest 객체로 변환 후 user-service에 전송
        UserRequest userRequest = new UserRequest(
                signUpRequest.getUsername(),
                signUpRequest.getPassword(),
                signUpRequest.getEmail(),
                signUpRequest.getSlackId(),
                signUpRequest.getRole()
        );
        // user-service로 회원 정보 전달
        return userClient.createUser(userRequest);

    }
}