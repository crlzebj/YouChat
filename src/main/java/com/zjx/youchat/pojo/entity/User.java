package com.zjx.youchat.pojo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author zjx
 * 
 */
@Data
public class User implements Serializable {
    // 
    private String id;
    // 
    private String email;
    // 
    private String password;
    // 
    private Integer status;
    // 
    private String nickName;
    // 
    private Integer permission;
    // 
    private Integer sex;
    // 
    private String personalSignature;
    // 
    private Integer areaCode;
    // 
    private LocalDateTime createTime;
    // 
    private LocalDateTime lastLoginTime;
    // 
    private LocalDateTime lastLogoutTime;
}
