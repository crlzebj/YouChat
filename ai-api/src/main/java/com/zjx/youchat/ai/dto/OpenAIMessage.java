package com.zjx.youchat.ai.dto;

import lombok.Data;

@Data
public class OpenAIMessage {
    private String role;
    private String content;
}
