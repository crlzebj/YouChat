package com.zjx.youchat.pojo.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String nickname;
    private String email;
    private String password;
    private String captchaKey;
    private String captchaValue;
}
