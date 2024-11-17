package com.zjx.youchat.pojo.po;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author zjx
 * 
 */
@Data
public class ChatGroup implements Serializable {
    // 
    private String id;
    // 
    private Integer status;
    // 
    private Integer permission;
    // 
    private String name;
    // 
    private String ownerId;
    // 
    private String groupNotice;
    // 
    private LocalDateTime createTime;
}
