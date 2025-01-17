package com.zjx.youchat.chat.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class GroupRegisterDTO implements Serializable {
    private String nickname;
    private String notice;
    private Integer authority;
    private MultipartFile avatarFile;
    private MultipartFile avatarThumbnailFile;
}
