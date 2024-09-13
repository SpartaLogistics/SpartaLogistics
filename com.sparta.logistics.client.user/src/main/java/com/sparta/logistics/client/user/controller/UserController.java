package com.sparta.logistics.client.user.controller;

import com.sparta.logistics.client.user.common.exception.UserException;
import com.sparta.logistics.client.user.model.validation.UserValid0001;
import com.sparta.logistics.client.user.service.UserService;
import com.sparta.logistics.client.user.model.UserVO;
import com.sparta.logistics.client.user.dto.UserRequest;
import com.sparta.logistics.common.controller.CustomApiController;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController extends CustomApiController {
    private final UserService userService;

    @PostMapping
    public ApiResult createUser(@RequestBody @Validated({UserValid0001.class}) UserRequest userRequest, Errors errors) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }
        try {
            UserVO response = userService.createUser(userRequest);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(response);
        } catch (UserException e){
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }

        return apiResult;
    }

    @GetMapping("/auth/{username}")
    public ApiResult findByUsername(@PathVariable String username) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try{
            UserVO user = userService.findByUsername(username);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(user);
        } catch (UserException e){
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }
    @GetMapping("/{userId}")
    public ApiResult getUserInfo(@PathVariable Long userId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try{
            UserVO user = userService.getUserInfo(userId);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(user);
        } catch (UserException e){
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }
    @DeleteMapping("/{userId}")
    public ApiResult deleteUser(@PathVariable Long userId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            userService.deleteUser(userId);
            apiResult.set(ApiResultError.NO_ERROR).setResultMessage("삭제되었습니다.");
        } catch (UserException e){
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }

        return apiResult;
    }

}
