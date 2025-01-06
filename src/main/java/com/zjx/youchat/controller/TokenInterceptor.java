package com.zjx.youchat.controller;

import com.alibaba.fastjson.JSON;
import com.zjx.youchat.constant.ExceptionConstant;
import com.zjx.youchat.constant.UserConstant;
import com.zjx.youchat.exception.BusinessException;
import com.zjx.youchat.pojo.dto.UserInfoDTO;
import com.zjx.youchat.util.ThreadLocalUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate redisTemplate;

    public TokenInterceptor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token == null) {
            throw new BusinessException(ExceptionConstant.TOKEN_FAILED);
        }
        String userInfoStr = redisTemplate.opsForValue().get(token);
        if (userInfoStr == null) {
            throw new BusinessException(ExceptionConstant.TOKEN_FAILED);
        }
        UserInfoDTO userInfo = JSON.parseObject(userInfoStr, UserInfoDTO.class);
        ThreadLocalUtil.setUserId(userInfo.getId());
        ThreadLocalUtil.setEmail(userInfo.getEmail());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
