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
    // 好友id/群组id
    private String contactId;
    // 类型 0：好友 1：群组
    private Integer type;
    // 好友权限 0：好友 1：拉黑 2：被拉黑 3：删除 4：被删除 5：非好友
    private Integer contactPermission;
    // 创建时间
    private LocalDateTime createTime;
    // 最后更新时间
    private LocalDateTime lastUpdateTime;
}
