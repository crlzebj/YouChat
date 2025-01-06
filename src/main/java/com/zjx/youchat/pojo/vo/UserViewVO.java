package com.zjx.youchat.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserViewVO implements Serializable {
    // 用户id
    private String id;
    // 昵称
    private String nickname;
    // 性别
    private Integer sex;
    // 地区
    private String area;
    // 个性签名
    private String personalSignature;
    // 用户状态 0：封禁 1：正常
    private Integer status;
}
