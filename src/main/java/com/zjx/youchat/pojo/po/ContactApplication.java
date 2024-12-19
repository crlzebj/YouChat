package com.zjx.youchat.pojo.po;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author zjx
 *
 */
@Data
public class ContactApplication implements Serializable {
    // 自增id
    private Integer id;
    // 申请人id
    private String applicantId;
    // 接收人id
    private String accepterId;
    // 好友id/群组id
    private String contactId;
    // 状态 0：未处理 1：已处理
    private Integer status;
    // 类型 0：好友 1：群组
    private Integer type;
    // 申请信息
    private String applicationInfo;
    // 最后申请时间
    private LocalDateTime lastApplicationTime;
}
