package com.zjx.youchat.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChatGroupRegisterDTO {
    private String id;
    private Integer permission;
    private String name;
    private String groupNotice;
    private MultipartFile avatarFile;
    private MultipartFile avatarThumbFile;
}
