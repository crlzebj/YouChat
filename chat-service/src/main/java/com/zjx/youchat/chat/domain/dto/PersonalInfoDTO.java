package com.zjx.youchat.chat.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PersonalInfoDTO implements Serializable {
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
    private Integer status;
    // 用户权限 0：禁止任何人添加我为好友 1：需要验证信息 2：无需验证信息
    private Integer authority;
    // 用户创建时间
    private LocalDateTime createTime;
}
