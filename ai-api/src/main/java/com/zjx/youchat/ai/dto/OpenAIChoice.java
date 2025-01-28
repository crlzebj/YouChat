package com.zjx.youchat.ai.dto;

import lombok.Data;

@Data
public class OpenAIChoice {
    private OpenAIMessage message;
    private String finish_reason;
    private String index;
}
