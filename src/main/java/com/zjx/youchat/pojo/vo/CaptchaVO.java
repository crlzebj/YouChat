package com.zjx.youchat.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaptchaVO implements Serializable {
    private String uuid;
    private String captcha;
}
