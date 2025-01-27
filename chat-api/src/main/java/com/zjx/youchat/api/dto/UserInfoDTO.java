package com.zjx.youchat.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoDTO implements Serializable {
    private String id;
    private String email;
}
