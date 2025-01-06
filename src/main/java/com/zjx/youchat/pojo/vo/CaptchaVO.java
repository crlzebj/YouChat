package com.zjx.youchat.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaptchaVO implements Serializable {
    private String captchaKey;
    private String captchaValue;
}
