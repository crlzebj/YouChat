package com.zjx.youchat.chat.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginVO implements Serializable {
    private String token;
}
