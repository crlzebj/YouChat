package com.zjx.youchat.controller;

import com.zjx.youchat.constant.UserConstant;
import com.zjx.youchat.exception.BusinessException;
import com.zjx.youchat.util.ThreadLocalUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            throw new BusinessException("身份验证不通过");
        }
        try {
            Claims payload = Jwts.parser()
                    .setSigningKey(UserConstant.SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            ThreadLocalUtil.setId(payload.get("id", String.class));
            ThreadLocalUtil.setEmail(payload.get("email", String.class));
            ThreadLocalUtil.setNickname(payload.get("nickname", String.class));
        } catch (Exception e) {
            throw new BusinessException("身份验证不通过");
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        ThreadLocalUtil.remove();
    }
}
