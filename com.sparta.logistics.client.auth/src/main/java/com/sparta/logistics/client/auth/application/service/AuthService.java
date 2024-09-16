package com.sparta.logistics.client.auth.application.service;

import com.sparta.logistics.client.auth.application.security.JWTUtil;
import com.sparta.logistics.client.auth.domain.model.UserVO;
import com.sparta.logistics.client.auth.infrastructure.feign.UserClient;
import com.sparta.logistics.client.auth.presentation.request.SignInRequest;
import com.sparta.logistics.client.auth.presentation.request.UserRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserClient userClient;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserVO signUp(UserRequest signUpRequest) {

        UserRequest userRequest = new UserRequest(
                signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail(),
                signUpRequest.getSlackId(),
                signUpRequest.getRole()
        );
        return userClient.createUser(userRequest);

    }

    public UserVO signIn(SignInRequest request, HttpServletResponse response) {
        UserVO userVO = userClient.findByUsername(request.getUsername());

        if (userVO == null || !passwordEncoder.matches(request.getPassword(), userVO.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = jwtUtil.createAccessToken(userVO.getId(), userVO.getUsername(), userVO.getRole(), response);

        return userVO;
    }
}