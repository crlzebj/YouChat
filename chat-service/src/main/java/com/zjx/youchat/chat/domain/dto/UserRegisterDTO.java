package com.zjx.youchat.chat.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterDTO implements Serializable {
    private String nickname;
    private String email;
    private String password;
    private String captchaKey;
    private String captchaValue;
}
