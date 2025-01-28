package com.zjx.youchat.ai.dto;

import lombok.Data;

@Data
public class OpenAIResponse {
    private String id;
    private String object;
    private String created;
    private String model;
    private OpenAIUsage usage;
    private OpenAIChoice[] choices;
}
