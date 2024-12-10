package com.zjx.youchat.pojo.po;

import java.io.Serializable;
import lombok.Data;

/**
 * @author zjx
 * 
 */
@Data
public class ChatMessage implements Serializable {
    // 消息自增ID
    private Long messageId;
    // 会话ID
    private String sessionId;
    // 消息类型
    private Integer messageType;
    // 消息内容
    private String messageContent;
    // 发送人ID
    private String sendUserId;
    // 发送人昵称
    private String sendUserNickName;
    // 发送时间
    private Long sendTime;
    // 接收人ID
    private String contactId;
    // 联系人类型 0：单聊 1：群聊
    private Integer contactType;
    // 文件大小
    private Long fileSize;
    // 文件名
    private String fileName;
    // 文件类型
    private Integer fileType;
    // 状态 0：正在发送 1：已发送
    private Integer status;
}
