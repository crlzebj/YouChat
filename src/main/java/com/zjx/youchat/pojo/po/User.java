package com.zjx.youchat.pojo.po;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author zjx
 *
 */
@Data
public class User implements Serializable {
    // id
    private String id;
    // 邮箱
    private String email;
    // 密码
    private String password;
    // 状态 0：封禁 1：正常
    private Integer status;
    // 加我为好友的方式 0：允许任何人 1：需要验证信息 2：禁止加我为好友
    private Integer permission;
    // 昵称
    private String nickname;
    // 性别
    private Integer sex;
    // 个性签名
    private String personalSignature;
    // 地区
    private String area;
    // 创建时间
    private LocalDateTime createTime;
    // 最后登录时间
    private LocalDateTime lastLoginTime;
    // 最后退出时间
    private LocalDateTime lastLogoutTime;
}
