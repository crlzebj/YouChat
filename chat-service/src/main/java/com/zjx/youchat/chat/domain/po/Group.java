package com.zjx.youchat.chat.domain.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zjx
 * 群组表
 */
@Data
public class Group implements Serializable {
    // 群组id
    private String id;
    // 昵称
    private String nickname;
    // 群主id
    private String ownerId;
    // 群公告
    private String notice;
    // 群组状态 0：封禁 1：正常
    private Integer status;
    // 群组权限 0：禁止任何人加入群组 1：需要验证信息 2：无需验证信息
    private Integer authority;
    // 群组创建时间
    private LocalDateTime createTime;
}
