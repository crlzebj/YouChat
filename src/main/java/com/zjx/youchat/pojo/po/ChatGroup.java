package com.zjx.youchat.pojo.po;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author zjx
 *
 */
@Data
public class ChatGroup implements Serializable {
    // id
    private String id;
    // 状态 0：封禁 1：正常
    private Integer status;
    // 加入群组的方式 0：任何人均可加入 1：需要验证信息 2：禁止加入群组
    private Integer permission;
    // 群主id
    private String ownerId;
    // 昵称
    private String nickname;
    // 群公告
    private String groupNotice;
    // 创建时间
    private LocalDateTime createTime;
}
