package com.zjx.youchat.chat.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class ResponseVO<T> implements Serializable {
    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据

    public static <T> ResponseVO<T> success() {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.code = 1;
        return responseVO;
    }

    public static <T> ResponseVO<T> success(T object) {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.code = 1;
        responseVO.data = object;
        return responseVO;
    }

    public static <T> ResponseVO<T> error(String msg) {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.code = 0;
        responseVO.msg = msg;
        return responseVO;
    }
}
