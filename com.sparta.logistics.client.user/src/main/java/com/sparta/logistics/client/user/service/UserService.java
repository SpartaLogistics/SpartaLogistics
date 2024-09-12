package com.sparta.logistics.client.user.service;

import com.sparta.logistics.client.user.repository.UserRepository;
import com.sparta.logistics.client.user.model.User;
import com.sparta.logistics.client.user.model.UserVO;
import com.sparta.logistics.client.user.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @Transactional
    public UserVO createUser(UserRequest userRequest) {
        log.info("Attempting to create user: {}", userRequest.getUsername());
        User user = User.createUser(
                userRequest.getUsername(),
                userRequest.getPassword(),
                userRequest.getEmail(),
                userRequest.getSlackId(),
                userRequest.getRole()
        );
        try {
            User saved = userRepository.save(user);
            log.info("User created successfully: {}", saved.getId());
            return saved.toUserVO();
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to create user due to data integrity violation", e);
            throw new IllegalArgumentException("User already exists");
        }
    }
    @Transactional(readOnly = true)
    public UserVO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        return user.toUserVO();
    }
    @Transactional(readOnly = true)
    public UserVO getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return user.toUserVO();
    }
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        user.delete(user.getId());
    }
}
