package com.zjx.youchat.chat.domain.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zjx
 * 用户表
 */
@Data
public class User implements Serializable {
    // 用户id
    private String id;
    // 邮箱
    private String email;
    // 密码
    private String password;
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
    // 用户最后登录时间
    private LocalDateTime lastLoginTime;
    // 用户最后退出时间
    private LocalDateTime lastLogoutTime;
}
