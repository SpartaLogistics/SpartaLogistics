package com.sparta.logistics.common.client;

import com.sparta.logistics.common.model.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/users/auth/{username}")
    UserVO findByUsername(@PathVariable("username") String username);
}
