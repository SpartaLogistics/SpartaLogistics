package com.sparta.logistics.client.user.service;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.sparta.logistics.client.user.common.exception.MessageException;
import com.sparta.logistics.client.user.common.exception.UserException;
import com.sparta.logistics.client.user.dto.MessageRequestDto;
import com.sparta.logistics.client.user.dto.MessageResponseDto;
import com.sparta.logistics.client.user.model.UserVO;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class SlackMessageService {

    private final UserService userService;
    private final MessageService messageService;

    public SlackMessageService(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @Value("${slack.token}")
    private String slackToken;

    public MessageResponseDto sendMessage(MessageRequestDto messageRequestDto, Long userId)
            throws IOException, SlackApiException, UserException, MessageException {
        Long receiverId = messageRequestDto.getReceiverId();
        UserVO user = userService.getUserInfo(receiverId);

        String slackId = user.getSlack_id();
        String messageText = messageRequestDto.getMessage();

        Slack slack = Slack.getInstance();
        MethodsClient methods = slack.methods(slackToken);

        log.info("Slack token: " + slackToken);
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(slackId)
                .text(messageText)
                .build();

        try {
            ChatPostMessageResponse response = methods.chatPostMessage(request);

            if (response.isOk()) {
                return messageService.createMessage(messageRequestDto, userId);
            } else {
                log.info("메시지 전송 실패:  {}", response.getError());
                throw new MessageException(ApiResultError.MESSAGE_SEND_ERROR);
            }
        } catch (IOException | SlackApiException e) {
            throw new MessageException(ApiResultError.MESSAGE_SEND_ERROR);
        }


    }
}
