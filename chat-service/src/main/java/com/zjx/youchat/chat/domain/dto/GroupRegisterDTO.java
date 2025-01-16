package com.zjx.youchat.chat.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class GroupRegisterDTO {
    private String nickname;
    private String notice;
    private Integer authority;
    private MultipartFile avatarFile;
    private MultipartFile avatarThumbnailFile;
}
