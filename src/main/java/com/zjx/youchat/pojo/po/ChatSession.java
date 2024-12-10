package com.zjx.youchat.pojo.po;

import java.io.Serializable;
import lombok.Data;

/**
 * @author zjx
 * 
 */
@Data
public class ChatSession implements Serializable {
    // 会话ID
    private String sessionId;
    // 最后接收的消息
    private String lastMessage;
    // 最后接收消息的时间
    private Long lastReceiveTime;
}
