package com.sparta.logistics.client.auth.application.service;

import com.sparta.logistics.client.auth.domain.model.UserVO;
import com.sparta.logistics.client.auth.infrastructure.feign.UserClient;
import com.sparta.logistics.client.auth.presentation.request.UserRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserClient userClient;
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
}