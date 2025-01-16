package com.zjx.youchat.websocket.constant.enums;

public enum WebSocketPackageEnum {
    LOGIN_INIT("登录初始化"),
    MESSAGE("消息"),
    REQUEST("请求"),
    REQUEST_DONE("请求处理结果"),
    ACCOUNT_BANNED("账号封禁"),
    GROUP_BANNED("群组封禁");

    public static WebSocketPackageEnum getInstanceByValue(Integer value) {
        return values()[value];
    }

    private final String desc;

    private WebSocketPackageEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
