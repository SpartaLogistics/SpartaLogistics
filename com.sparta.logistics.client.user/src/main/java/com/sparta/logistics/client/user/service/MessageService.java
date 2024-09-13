package com.sparta.logistics.client.user.service;

import com.sparta.logistics.client.user.dto.MessageRequestDto;
import com.sparta.logistics.client.user.dto.MessageResponse;
import com.sparta.logistics.client.user.model.Message;
import com.sparta.logistics.client.user.model.User;
import com.sparta.logistics.client.user.repository.MessageRepository;
import com.sparta.logistics.client.user.repository.UserRepository;
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

    public MessageResponse createMessage(MessageRequestDto request, Long userid) {
        User sender = userRepository.findById(userid)
                .orElseThrow(()-> new IllegalArgumentException("User not found"));
        User receiver = userRepository.findBySlack_id(request.getSlackId())
                .orElseThrow(()-> new IllegalArgumentException("User not found"));
        Message message = Message.createMessage(sender, receiver, request.getMessage());

        return MessageResponse.from(messageRepository.save(message));
    }
    public Page<MessageResponse> searchMessages(Long senderId, Long receiverId, String context, Pageable pageable) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        Page<Message> messages = messageRepository.findBySenderAndReceiverAndMessageContaining(sender, receiver, context, pageable);
        return messages.map(MessageResponse::from); // Message 엔티티를 MessageResponse로 변환
    }

    public MessageResponse deleteMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        message.softDelete();
        return MessageResponse.from(message);
    }
}
