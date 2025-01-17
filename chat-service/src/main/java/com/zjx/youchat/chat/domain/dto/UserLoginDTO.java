package com.zjx.youchat.chat.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {
    private String email;
    private String password;
    private String captchaKey;
    private String captchaValue;
}
