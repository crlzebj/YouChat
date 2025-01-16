package com.zjx.youchat.chat.domain.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
    private String captchaKey;
    private String captchaValue;
}
