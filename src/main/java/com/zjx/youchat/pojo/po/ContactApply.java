package com.zjx.youchat.pojo.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zjx
 * 联系人申请表
 */
@Data
public class ContactApply implements Serializable {
    // 联系人申请id
    private Long id;
    // 联系人申请发起者id
    private String initiatorId;
    // 联系人申请接受者id
    private String accepterId;
    // 好友申请则为用户id 群组申请则为群组id
    private String contactId;
    // 申请信息
    private String applyInfo;
    // 最后申请时间
    private LocalDateTime lastApplyTime;
    // 联系人申请状态 0：未处理 1：已处理
    private Integer status;
    // 联系人申请类型 0：好友申请 1：群组申请
    private Integer applyType;
}
