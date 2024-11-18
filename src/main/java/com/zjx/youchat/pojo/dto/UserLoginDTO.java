package com.zjx.youchat.pojo.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
    private String captchaKey;
    private String captchaValue;
}
