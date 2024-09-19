package com.sparta.logistics.client.user.service;

import com.sparta.logistics.client.user.common.exception.UserException;
import com.sparta.logistics.client.user.dto.AiRequestDto;
import com.sparta.logistics.client.user.dto.MessageRequestDto;
import com.sparta.logistics.client.user.dto.MessageResponseDto;
import com.sparta.logistics.client.user.model.Message;
import com.sparta.logistics.client.user.model.User;
import com.sparta.logistics.client.user.repository.MessageRepository;
import com.sparta.logistics.client.user.repository.UserRepository;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    //private final AiClient aiClient;

    public MessageResponseDto createMessage(MessageRequestDto request, Long userid) throws UserException {
        User sender = userRepository.findById(userid)
                .orElseThrow(()-> new UserException(ApiResultError.USER_NO_EXIST));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(()-> new UserException(ApiResultError.USER_NO_EXIST));
        Message message = Message.createMessage(sender, receiver, request.getMessage());

        return MessageResponseDto.from(messageRepository.save(message));
    }
    public Page<MessageResponseDto> searchMessages(Long senderId, Long receiverId, String context, Pageable pageable) throws UserException {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserException(ApiResultError.USER_NO_EXIST));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new UserException(ApiResultError.USER_NO_EXIST));

        Page<Message> messages = messageRepository.findBySenderAndReceiverAndMessageContaining(sender, receiver, context, pageable);
        return messages.map(MessageResponseDto::from); // Message 엔티티를 MessageResponse로 변환
    }

    public MessageResponseDto deleteMessage(UUID messageId) throws UserException {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new UserException(ApiResultError.MESSAGE_NO_EXIST));
        message.softDelete();
        return MessageResponseDto.from(message);
    }

//    public ApiResult getCorrectMessage(String message) throws UserException {
//        AiRequestDto aiRequestDto = new AiRequestDto();
//        aiRequestDto.setService("user-service");
//        aiRequestDto.setQuestion(message + "앞에 말의 문법이 틀린게 있다면 고쳐줘");
//        return aiClient.createAI(aiRequestDto);
//    }
}
