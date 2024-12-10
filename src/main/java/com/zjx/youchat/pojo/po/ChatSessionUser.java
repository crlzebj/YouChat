package com.zjx.youchat.pojo.po;

import java.io.Serializable;
import lombok.Data;

/**
 * @author zjx
 * 
 */
@Data
public class ChatSessionUser implements Serializable {
    // 用户ID
    private String userId;
    // 联系人ID
    private String contactId;
    // 会话ID
    private String sessionId;
    // 联系人名称
    private String contactName;
}
