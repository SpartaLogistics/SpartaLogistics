package com.sparta.logistics.client.user;
import java.io.IOException;
import java.net.URL;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.api.ApiTestResponse;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

@Slf4j
public class Test {
    public static void main(String[] args) throws IOException, SlackApiException {
        String urlStr = "https://slack.com/api/conversations.list?pretty=1";
        String token = "xoxb-7750835290354-7747978570325-KtuMLBYr0jJ4tjVf7BHvdP1e";
        String userId =  "D07NFF01L1F";// "U07MXCNPG22";//"U07MW62H72S";

        //    sendMessageToUser(slackToken, userId, "test!!!");

        //String channelId = "D07NFF01L1F";    // 다이렉트 메시지 채널 ID

        Slack slack = Slack.getInstance();
        MethodsClient methods = slack.methods(token);

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(userId)
                .text("안녕하세요! 이것은 Java로 보낸 Slack 메시지입니다.")
                .build();

        try {
            ChatPostMessageResponse response = methods.chatPostMessage(request);
            if (response.isOk()) {
                System.out.println("메시지 전송 성공");
            } else {
                System.out.println("메시지 전송 실패: " + response.getError());
            }
        } catch (IOException | SlackApiException e) {
            System.out.println("오류 발생: " + e.getMessage());
        }



    }

    public static void sendMessageToUser(String token, String userId, String text) {
        try {
            Slack slack = Slack.getInstance();
            MethodsClient methods = slack.methods(token);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(userId) // 사용자 ID를 채널로 사용
                    .text(text)
                    .build();

            ChatPostMessageResponse response = methods.chatPostMessage(request);

            if (response.isOk()) {
                System.out.println("메시지 전송 성공: " + response.getMessage().getText());
            } else {
                System.out.println("메시지 전송 실패: " + response.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
