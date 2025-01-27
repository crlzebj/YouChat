package com.zjx.youchat.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zjx
 * 会话表
 */
@Data
public class Session implements Serializable {
    // 会话id
    private String id;
    // 会话发起者id
    private String initiatorId;
    // 会话接收者id
    private String accepterId;
    // 最后的消息
    private String lastMessage;
    // 最后的消息的发送时间
    private LocalDateTime lastSendTime;
}
