package com.zjx.youchat.chat.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaptchaVO implements Serializable {
    private String captchaKey;
    private String captchaValue;
}
