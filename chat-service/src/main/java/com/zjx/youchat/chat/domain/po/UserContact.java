package com.zjx.youchat.chat.domain.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserContact implements Serializable {
    // 联系人发起者id
    private String initiatorId;
    // 联系人接受者id
    private String accepterId;
    // 联系人状态
    private Integer status;
    // 联系人创建时间
    private LocalDateTime createTime;
    // 用户id
    private String id;
    // 邮箱
    private String email;
    // 昵称
    private String nickname;
    // 性别
    private Integer sex;
    // 地区
    private String area;
    // 个性签名
    private String personalSignature;
    // 用户状态 0：封禁 1：正常
    private Integer userStatus;
}
