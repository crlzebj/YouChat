package com.zjx.youchat.pojo.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String email;
    private String password;
    private String nickname;
    private String captchaKey;
    private String captchaValue;
}
