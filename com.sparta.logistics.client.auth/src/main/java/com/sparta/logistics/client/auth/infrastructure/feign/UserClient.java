package com.sparta.logistics.client.auth.infrastructure.feign;

import com.sparta.logistics.client.auth.domain.model.UserVO;
import com.sparta.logistics.client.auth.presentation.request.UserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserClient {

    @PostMapping("/users")
    UserVO createUser(@RequestBody UserRequest userRequest);
}
