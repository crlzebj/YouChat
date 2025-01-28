package com.zjx.youchat.ai;

import com.zjx.youchat.ai.dto.OpenAIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DeepSeekAPI {
    public static String doPost(String msg) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.deepseek.com/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer sk-f0f55135bff94c278676ba43bd6536bc");

        // 构建请求体
        Map<String, Object> body = new HashMap<>();
        body.put("model", "deepseek-chat");
        // 设置消息内容
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant.");
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", msg);
        body.put("messages", new Map[]{systemMessage, userMessage});
        body.put("stream", false);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<OpenAIResponse> response = restTemplate.exchange(url,
                HttpMethod.POST,
                request,
                OpenAIResponse.class
        );

        return response.getBody().getChoices()[0].getMessage().getContent();
    }

    public static void main(String[] args) {
        System.out.println(doPost("如何准备Java面试八股文"));
    }
}
