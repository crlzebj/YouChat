package com.zjx.youchat.pojo.po;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author zjx
 * 
 */
@Data
public class UserContact implements Serializable {
    // 用户id
    private String userId;
    // 联系人或群聊id
    private String contactId;
    // 联系人类型
    private Integer contactType;
    // 联系人权限
    private Integer contactPermission;
    // 创建时间
    private LocalDateTime createTime;
    // 最后更新时间
    private LocalDateTime lastUpdateTime;
}
