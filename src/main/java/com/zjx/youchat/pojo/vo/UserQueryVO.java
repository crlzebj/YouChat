package com.zjx.youchat.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserQueryVO implements Serializable {
    // 用户id
    private String id;
    // 昵称
    private String nickname;
    // 用户状态 0：封禁 1：正常
    private Integer status;
    // 用户权限 0：禁止任何人添加我为好友 1：需要验证信息 2：无需验证信息
    private Integer authority;
}
