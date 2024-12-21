package com.zjx.youchat.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.zjx.youchat.constant.UserConstant;
import com.zjx.youchat.exception.BusinessException;
import com.zjx.youchat.pojo.dto.WebsocketPackageDTO;
import com.zjx.youchat.websocket.service.ChannelService;
import com.zjx.youchat.websocket.service.RedissonService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
import org.springframework.stereotype.Component;

@Slf4j
@ChannelHandler.Sharable
@Component
public class WebSocketChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Autowired
    private ChannelService channelService;
    @Autowired
    private RedissonService redissonService;

    private Claims login(String uri) {
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
            Claims claims = null;
            try {
                claims = Jwts.parser()
                        .setSigningKey(UserConstant.SECRET_KEY)
                        .parseClaimsJws(value)
                        .getBody();
            } catch (Exception e) {
                return null;
            }
            return claims;
        }
        return null;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (!(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete)) {
            return;
        }
        WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete =
                (WebSocketServerProtocolHandler.HandshakeComplete) evt;
        Claims claims = login(handshakeComplete.requestUri());
        if (claims == null) {
            ctx.channel().close();
            throw new BusinessException("身份验证未通过");
        }
        String userId = claims.get("id", String.class);
        Channel channel = ctx.channel();
        channelService.registerChannel(userId, channel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                TextWebSocketFrame textWebSocketFrame) throws Exception {
        Attribute<String> attribute = channelHandlerContext.channel()
                .attr(AttributeKey.valueOf(channelHandlerContext.channel().id().toString()));
        String userId = attribute.get();
        log.info("{}: {}", userId, textWebSocketFrame.text());
        WebsocketPackageDTO websocketPackageDTO = JSON.parseObject(textWebSocketFrame.text(),
                WebsocketPackageDTO.class);
        if (websocketPackageDTO == null) {
            throw new BusinessException("JSON解析出错");
        }
        redissonService.publish(websocketPackageDTO);
        // String responseMessage = new JfchataiRobotServiceImpl().chat(textWebSocketFrame.text());
        // log.info(responseMessage);
        // channelHandlerContext.writeAndFlush(new TextWebSocketFrame(responseMessage));
    }
}
