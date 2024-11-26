package com.zjx.youchat.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ContactVO implements Serializable {
    private String id;
    private int type;
    private int status;
    private String name;
    private String sex;
    private int areaCode;
    private boolean isFriend;
}
