package com.zjx.youchat.websocket;

import com.alibaba.fastjson.JSON;
import com.zjx.youchat.constant.ExceptionConstant;
import com.zjx.youchat.exception.BusinessException;
import com.zjx.youchat.pojo.dto.UserInfoDTO;
import com.zjx.youchat.pojo.dto.WebSocketPackage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@ChannelHandler.Sharable
@Component
public class WebSocketChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ChannelManager channelManager;

    @Autowired
    private RedissonService redissonService;

    private UserInfoDTO authenticate(String uri) {
        if (!uri.contains("?")) {
            return null;
        }
        String paramsStr = uri.split("\\?")[1];
        String[] params = paramsStr.split("&");
        for (String param : params) {
            String key = param.split("=")[0];
            String value = param.split("=")[1];
            if (key.compareTo("token") != 0) {
                continue;
            }
            String userInfoStr = redisTemplate.opsForValue().get(value);
            if (userInfoStr == null) {
                return null;
            }
            return JSON.parseObject(userInfoStr, UserInfoDTO.class);
        }
        return null;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        AttributeKey<String> attributeKey = AttributeKey.valueOf("userId");
        String userId = ctx.channel().attr(attributeKey).get();
        channelManager.cancelChannel(userId);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (!(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete)) {
            return;
        }
        WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete =
                (WebSocketServerProtocolHandler.HandshakeComplete) evt;
        UserInfoDTO userInfo = authenticate(handshakeComplete.requestUri());
        if (userInfo == null) {
            log.info(ExceptionConstant.TOKEN_FAILED);
            ctx.channel().close();
            return;
        }
        String userId = userInfo.getId();
        Channel channel = ctx.channel();

        // 将userId绑定到Channel上
        AttributeKey<String> attributeKey = null;
        if (AttributeKey.exists("userId")) {
            attributeKey = AttributeKey.valueOf("userId");
        } else {
            attributeKey = AttributeKey.newInstance("userId");
        }
        channel.attr(attributeKey).setIfAbsent(userId);

        //注册Channel
        channelManager.registerChannel(userId, channel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                TextWebSocketFrame textWebSocketFrame) throws Exception {
        Attribute<String> attribute = channelHandlerContext.channel()
                .attr(AttributeKey.valueOf("userId"));
        String userId = attribute.get();
        log.info("{}: {}", userId, textWebSocketFrame.text());

        try {
            WebSocketPackage websocketPackage = JSON.parseObject(textWebSocketFrame.text(),
                    WebSocketPackage.class);
            redissonService.publish(websocketPackage);
        } catch (Exception e) {
            log.info("WebSocket服务器异常：{}", ExceptionConstant.WEBSOCKET_PACKAGE_FORMAT_ERROR);
        }
    }
}
