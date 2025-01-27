package com.zjx.youchat.chat.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class WSPackage<T> implements Serializable {
    private Integer type;
    private String receiverId;
    private T data;
}
