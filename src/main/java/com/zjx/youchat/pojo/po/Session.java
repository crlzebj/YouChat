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
    private String initiatorId;
    // 用户昵称
    private String initiatorNickname;
    // 联系人id
    private String accepterId;
    // 联系人昵称
    private String accepterNickname;
    // id
    private String id;
    // 最后接收的消息
    private String lastMessage;
    // 最后接收消息的时间
    private LocalDateTime lastReceiveTime;
}
