package com.zjx.youchat.chat.controller;

import com.alibaba.fastjson.JSON;
import com.zjx.youchat.chat.constant.ExceptionConstant;
import com.zjx.youchat.chat.constant.UserConstant;
import com.zjx.youchat.chat.exception.BusinessException;
import com.zjx.youchat.chat.domain.dto.UserInfoDTO;
import com.zjx.youchat.chat.util.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        ThreadLocalUtil.setUserId(request.getHeader("user-id"));
        ThreadLocalUtil.setEmail(request.getHeader("email"));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
