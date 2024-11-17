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
    // 账号状态
    private Integer status;
    // 联系人申请权限
    private Integer permission;
    // 昵称
    private String nickname;
    // 性别
    private Integer sex;
    // 个性签名
    private String personalSignature;
    // 地区
    private Integer areaCode;
    // 创建时间
    private LocalDateTime createTime;
    // 最后登录时间
    private LocalDateTime lastLoginTime;
    // 最后退出时间
    private LocalDateTime lastLogoutTime;
}
