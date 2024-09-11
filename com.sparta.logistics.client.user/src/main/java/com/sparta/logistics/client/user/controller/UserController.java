package com.sparta.logistics.client.user.controller;

import com.sparta.logistics.client.user.service.UserService;
import com.sparta.logistics.client.user.model.UserVO;
import com.sparta.logistics.client.user.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserVO> createUser(@RequestBody UserRequest userRequest) {
        UserVO userVO = userService.createUser(userRequest);
        return ResponseEntity.ok(userVO);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserVO> findByUsername(@PathVariable String username) {
        UserVO user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
