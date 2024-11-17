package com.zjx.youchat.pojo.po;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author zjx
 * 
 */
@Data
public class ContactApply implements Serializable {
    // id
    private Integer id;
    // 申请人id
    private String applicantId;
    // 接收人id
    private String accepterId;
    // 群组id
    private String contactId;
    // 状态
    private Integer status;
    // 联系人申请类型
    private Integer contactType;
    // 申请信息
    private String applyInfo;
    // 最后申请时间
    private LocalDateTime lastApplyTime;
}
