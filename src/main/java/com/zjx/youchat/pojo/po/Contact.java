package com.zjx.youchat.pojo.po;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author zjx
 *
 */
@Data
public class Contact implements Serializable {
    // 发起者id
    private String initiatorId;
    // 发起者昵称
    private String initiatorNickname;
    // 接受者id
    private String accepterId;
    // 接受者昵称
    private String accepterNickname;
    // 类型 0：好友 1：群组
    private Integer type;
    // 好友权限 0：好友 1：拉黑 2：被拉黑 3：删除 4：被删除 5：非好友
    private Integer status;
    // 创建时间
    private LocalDateTime createTime;
    // 最后更新时间
    private LocalDateTime lastUpdateTime;
}
