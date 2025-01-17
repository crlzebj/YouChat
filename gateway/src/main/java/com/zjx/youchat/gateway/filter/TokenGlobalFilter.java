package com.zjx.youchat.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.zjx.youchat.chat.api.constant.UserConstant;
import com.zjx.youchat.chat.api.domain.dto.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class TokenGlobalFilter implements GlobalFilter, Ordered {
    private final StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 排除路径
        Set<String> excludePaths = new HashSet<>();
        excludePaths.add("/user/captcha");
        excludePaths.add("/user/register");
        excludePaths.add("/user/login");
        if (excludePaths.contains(request.getPath().toString())) {
            return chain.filter(exchange);
        }

        HttpHeaders headers = request.getHeaders();
        List<String> token = headers.get("token");

        // 无token
        if (token == null || token.isEmpty()) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        String userInfoStr = redisTemplate.opsForValue().get(UserConstant.TOKEN_PREFIX + token.get(0));

        // token不正确
        if (userInfoStr == null || userInfoStr.isEmpty()) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        UserInfoDTO userInfoDTO = JSON.parseObject(userInfoStr, UserInfoDTO.class);
        ServerWebExchange serverWebExchange = exchange.mutate().request(req -> {
            req.header("user-id", userInfoDTO.getId());
            req.header("email", userInfoDTO.getEmail());
        }).build();

        return chain.filter(serverWebExchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
