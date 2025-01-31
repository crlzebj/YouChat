package com.zjx.youchat.chat.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ContactApplyDTO implements Serializable {
    // 好友申请则为用户id 群组申请则为群组id
    private String contactId;
    // 申请信息
    private String applyInfo;
    // 联系人申请类型 0：好友申请 1：群组申请
    private Integer applyType;
}
