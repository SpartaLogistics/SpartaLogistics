package com.sparta.logistics.client.user.controller;

import com.sparta.logistics.client.user.common.exception.UserException;
import com.sparta.logistics.client.user.dto.UserRequest;
import com.sparta.logistics.client.user.model.UserVO;
import com.sparta.logistics.client.user.model.validation.UserValid0001;
import com.sparta.logistics.client.user.service.UserService;
import com.sparta.logistics.common.controller.CustomApiController;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.model.RoleCheck;
import com.sparta.logistics.common.type.ApiResultError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "User 요청 API", description = "User 요청 내역 관리 목적의 API Docs")
@RequiredArgsConstructor
public class UserController extends CustomApiController {
    private final UserService userService;

    @Operation(summary = "유저 생성", description = "유저 생성")
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
    @Operation(summary = "로그인시 유저 검색", description = "로그인 유저 검색")
    @GetMapping("/auth/{username}")
    public UserVO findByUsername(@PathVariable String username) {
        System.out.println("Requested Username: " + username);
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try{
            UserVO user = userService.findByUsername(username);
            System.out.println("Found User: " + user);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(user);
            return user;
        } catch (UserException e){
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return null;
    }
    @RoleCheck(roles = {"CUSTOMER", "MASTER"})
    @Operation(summary = "userId로 유저 검색", description = "userId로 유저 검색")
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
    @Operation(summary = "userId로 유저 삭제", description = "userId로 유저 삭제")
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
