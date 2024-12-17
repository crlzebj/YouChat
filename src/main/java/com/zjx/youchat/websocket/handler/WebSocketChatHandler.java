package com.zjx.youchat.websocket.handler;

import com.zjx.youchat.constant.UserConstant;
import com.zjx.youchat.service.impl.JfchataiRobotServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
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
        if (login(handshakeComplete.requestUri()) == null) {
            ctx.channel().close();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        log.info(textWebSocketFrame.text());
        String responseMessage = new JfchataiRobotServiceImpl().chat(textWebSocketFrame.text());
        log.info(responseMessage);
        channelHandlerContext.writeAndFlush(new TextWebSocketFrame(responseMessage));
    }
}
