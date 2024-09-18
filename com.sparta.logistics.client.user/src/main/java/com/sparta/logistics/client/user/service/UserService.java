package com.sparta.logistics.client.user.service;

import com.sparta.logistics.client.user.common.exception.UserException;
import com.sparta.logistics.client.user.repository.UserRepository;
import com.sparta.logistics.client.user.model.User;
import com.sparta.logistics.client.user.model.UserVO;
import com.sparta.logistics.client.user.dto.UserRequestDto;
import com.sparta.logistics.common.type.ApiResultError;
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
    public UserVO createUser(UserRequestDto userRequest) throws UserException {
        log.info("Attempting to create user: {}", userRequest.getUsername());
        User user = User.createUser(
                userRequest.getUsername(),
                userRequest.getPassword(),
                userRequest.getEmail(),
                userRequest.getSlackId(),
                userRequest.getRole(),
                false
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
    public UserVO findByUsername(String username) throws UserException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(ApiResultError.USER_NO_EXIST));
        return user.toUserVO();
    }
    @Transactional(readOnly = true)
    public UserVO getUserInfo(Long userId) throws UserException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ApiResultError.USER_NO_EXIST));
        return user.toUserVO();
    }
    @Transactional
    public void deleteUser(Long userId) throws UserException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ApiResultError.USER_NO_EXIST));
        user.softDelete();
    }
}
