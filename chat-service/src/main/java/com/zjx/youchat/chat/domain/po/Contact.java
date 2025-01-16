package com.zjx.youchat.chat.domain.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zjx
 * 联系人表
 */
@Data
public class Contact implements Serializable {
    // 联系人发起者id
    private String initiatorId;
    // 联系人接受者id
    private String accepterId;
    // 联系人状态
    private Integer status;
    // 联系人类型
    private Integer contactType;
    // 联系人创建时间
    private LocalDateTime createTime;
    // 联系人状态最后更新时间
    private LocalDateTime lastUpdateTime;
}
