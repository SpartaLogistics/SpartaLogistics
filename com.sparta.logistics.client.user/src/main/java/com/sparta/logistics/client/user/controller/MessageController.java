package com.sparta.logistics.client.user.controller;


import com.sparta.logistics.client.user.common.exception.UserException;
import com.sparta.logistics.client.user.dto.MessageRequestDto;
import com.sparta.logistics.client.user.dto.MessageResponseDto;
import com.sparta.logistics.client.user.dto.SearchRequestDto;
import com.sparta.logistics.client.user.model.User;
import com.sparta.logistics.client.user.model.validation.MessageValid0001;
import com.sparta.logistics.client.user.model.validation.MessageValid0002;
import com.sparta.logistics.client.user.repository.UserRepository;
import com.sparta.logistics.client.user.service.MessageService;
import com.sparta.logistics.common.controller.CustomApiController;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController extends CustomApiController {
    private final MessageService messageService;
    private final UserRepository userRepository;

    @PostMapping
    public ApiResult createMessage(@RequestBody @Validated({MessageValid0001.class}) MessageRequestDto request, @RequestHeader("X-User-Name") String username, Errors errors){
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            MessageResponseDto response = messageService.createMessage(request, user.getId());
            apiResult.set(ApiResultError.NO_ERROR).setResultData(response);
        } catch (UserException e){
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @GetMapping("/messages/search")
    public ApiResult searchMessages(
            @RequestBody @Validated({MessageValid0002.class}) SearchRequestDto request,
            Pageable pageable, @RequestHeader("X-User-Name") String username, Errors errors
    ) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            Page<MessageResponseDto> messages = messageService.searchMessages(user.getId(), request.getReceiverId(), request.getContext(), pageable);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(messages);
        } catch (UserException e){
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }

        return apiResult;
    }

    @DeleteMapping("/{messageId}")
    public ApiResult deleteMessage(@PathVariable UUID messageId){
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try{
            messageService.deleteMessage(messageId);
            apiResult.set(ApiResultError.NO_ERROR).setResultMessage("삭제되었습니다.");
        } catch (UserException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }
}
