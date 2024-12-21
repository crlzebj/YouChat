package com.zjx.youchat.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class WebsocketPackageDTO<T> implements Serializable {
    private Integer type;
    private String receiverId;
    private T data;
}
