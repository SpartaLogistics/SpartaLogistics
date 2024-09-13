package com.sparta.logistics.client.user.controller;

import com.sparta.logistics.client.user.dto.MessageRequestDto;
import com.sparta.logistics.client.user.dto.MessageResponse;
import com.sparta.logistics.client.user.dto.SearchRequest;
import com.sparta.logistics.client.user.dto.SearchRequest;
import com.sparta.logistics.client.user.model.Message;
import com.sparta.logistics.client.user.model.User;
import com.sparta.logistics.client.user.repository.UserRepository;
import com.sparta.logistics.client.user.service.MessageService;
import com.sparta.logistics.client.user.service.UserService;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(@RequestBody MessageRequestDto request, @AuthenticationPrincipal SecurityUserDetails userDetails){
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        MessageResponse response = messageService.createMessage(request, user.getId());
    }

    @GetMapping("/messages/search")
    public ResponseEntity<Page<MessageResponse>> searchMessages(
            @RequestBody SearchRequest request,
            Pageable pageable,  @AuthenticationPrincipal SecurityUserDetails userDetails
    ) {
        Page<MessageResponse> messages = messageService.searchMessages(userDetails.getId(), request.getReceiverId(), request.getContext(), pageable);
        return ResponseEntity.ok(messages);
    }
    @DeleteMapping("/{messageId}")
    public ResponseEntity<MessageResponse> deleteMessage(@PathVariable UUID messageId){
        MessageResponse message = messageService.deleteMessage(messageId);
        return ResponseEntity.ok(message);
    }
}
