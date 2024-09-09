package com.sparta.logistics.client.user.application.service;

import com.sparta.logistics.client.user.domain.Repository.UserRepository;
import com.sparta.logistics.client.user.domain.model.User;
import com.sparta.logistics.client.user.domain.model.UserVO;
import com.sparta.logistics.client.user.presentation.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;
    public UserVO createUser(UserRequest userRequest) {
        User user = User.createUser(
                userRequest.getUsername(),
                userRequest.getPassword(),
                userRequest.getEmail(),
                userRequest.getSlackId(),
                userRequest.getRole()
                );
        try {

            User saved = userRepository.save(user);
            return saved.toUserVO();

        } catch (DataIntegrityViolationException e) {

            throw new IllegalArgumentException("Already exists");

        }
    }
}
