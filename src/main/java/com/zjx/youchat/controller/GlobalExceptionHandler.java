package com.zjx.youchat.controller;

import com.zjx.youchat.exception.BusinessException;
import com.zjx.youchat.pojo.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 捕获业务异常
     * @param exception
     * @return
     */
    @ExceptionHandler
    public ResponseVO exceptionHandler(BusinessException exception){
        log.error("异常信息：{}", exception.getMessage());
        return ResponseVO.error(exception.getMessage());
    }
}
