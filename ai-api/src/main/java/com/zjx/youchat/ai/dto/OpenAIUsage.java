package com.zjx.youchat.ai.dto;

import lombok.Data;

@Data
public class OpenAIUsage {
    private String prompt_token;
    private String completion_tokens;
    private String total_tokens;
}
