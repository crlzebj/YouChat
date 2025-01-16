package com.zjx.youchat.chat.domain.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zjx
 * 消息表
 */
@Data
public class Message implements Serializable {
    // 消息id
    private Long id;
    // 消息所属会话id
    private String sessionId;
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
    // 消息状态
    private Integer status;
}
