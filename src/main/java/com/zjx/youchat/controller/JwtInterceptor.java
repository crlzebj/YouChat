package com.zjx.youchat.controller;

import com.zjx.youchat.constant.UserConstant;
import com.zjx.youchat.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");
        if (token == null) {
            throw new BusinessException("身份验证不通过");
        }
        try {
            Claims payload = Jwts.parser().setSigningKey(UserConstant.SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new BusinessException("身份验证不通过");
        }

        return true;
    }
}
