package com.zjx.youchat.pojo.po;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * @author zjx
 *
 */
@Data
public class Session implements Serializable {
    // 用户id
    private String userId;
    // 联系人id
    private String contactId;
    // 联系人昵称
    private String contactNickname;
    // id
    private String id;
    // 最后接收的消息
    private String lastMessage;
    // 最后接收消息的时间
    private LocalDateTime lastReceiveTime;
}
