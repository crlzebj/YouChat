package com.zjx.youchat.chat.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MessageSendDTO implements Serializable {
    // 消息发送者id
    private String senderId;
    // 消息接收者id
    private String receiverId;
    // 消息发送时间
    private LocalDateTime sendTime;
    // 消息内容
    private String content;
    // 文件名
    private String filename;
    // 文件类型
    private Integer fileType;
    // 文件大小
    private Integer fileSize;
    // 接受者类型
    private Integer receiverType;
    // 消息类型
    private Integer type;
}
