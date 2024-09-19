package com.sparta.logistics.client.user.controller;


import com.sparta.logistics.client.user.common.exception.MessageException;
import com.sparta.logistics.client.user.common.exception.UserException;
import com.sparta.logistics.client.user.dto.MessageRequestDto;
import com.sparta.logistics.client.user.dto.MessageResponseDto;
import com.sparta.logistics.client.user.dto.SearchRequestDto;
import com.sparta.logistics.client.user.model.User;
import com.sparta.logistics.client.user.model.validation.MessageValid0001;
import com.sparta.logistics.client.user.model.validation.MessageValid0002;
import com.sparta.logistics.client.user.repository.UserRepository;
import com.sparta.logistics.client.user.service.MessageService;
import com.sparta.logistics.client.user.service.SlackMessageService;
import com.sparta.logistics.common.controller.CustomApiController;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.type.ApiResultError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Message 요청 API", description = "Message 요청 내역 관리 목적의 API Docs")
public class MessageController extends CustomApiController {
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final SlackMessageService slackMessageService;

    @Operation(summary = "Message 생성", description = "Message 생성")
    @PostMapping
    public ApiResult createMessage(@RequestBody @Validated({MessageValid0001.class}) MessageRequestDto request, @RequestHeader("X-User-Name") String username, Errors errors){
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            //MessageResponseDto response = messageService.createMessage(request, user.getId());
            MessageResponseDto response = slackMessageService.sendMessage(request, user.getId());
            apiResult.set(ApiResultError.NO_ERROR).setResultData(response);
        } catch (UserException e){
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        } catch (MessageException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        } catch (Exception e){
            apiResult.set(ApiResultError.ERROR_DEFAULT).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @Operation(summary = "Message 검색", description = "검색어로 메시지 검색")
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

    @Operation(summary = "Message 삭제", description = "Message 삭제")
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

    @GetMapping("/optimal")
    @Operation(summary = "메시지 문법 AI 조회 API", description = "메시지 내용의 문법이 맞는지 확인하는 ai 사용입니다.")
    public ApiResult getCorrectMessage(@RequestParam String message) {
        try {
            return messageService.getCorrectMessage(message);
        } catch (UserException e) {
            return new ApiResult(e.getCode()).setResultMessage(e.getMessage());
        }
    }
}
