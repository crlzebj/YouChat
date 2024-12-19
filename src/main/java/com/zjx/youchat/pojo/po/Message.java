package com.zjx.youchat.pojo.po;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author zjx
 *
 */
@Data
public class Message implements Serializable {
    // 自增id
    private Long id;
    // 所属会话id
    private String sessionId;
    // 发送人ID
    private String senderId;
    // 发送人昵称
    private String senderNickName;
    // 接收人ID
    private String receiverId;
    // 类型
    private Integer type;
    // 内容
    private String content;
    // 文件名
    private String fileName;
    // 文件类型
    private Integer fileType;
    // 文件大小
    private Long fileSize;
    // 发送时间
    private LocalDateTime sendTime;
    // 联系人类型 0：好友 1：群组
    private Integer contactType;
    // 状态 0：正在发送 1：已送达
    private Integer status;
}
