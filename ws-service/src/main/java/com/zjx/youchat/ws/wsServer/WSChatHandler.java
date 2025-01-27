package com.zjx.youchat.ws.wsServer;

import com.alibaba.fastjson.JSON;
import com.zjx.youchat.api.client.ChatClient;
import com.zjx.youchat.api.dto.PersonalInfoDTO;
import com.zjx.youchat.api.dto.WSInitDTO;
import com.zjx.youchat.api.dto.GroupContact;
import com.zjx.youchat.api.dto.Message;
import com.zjx.youchat.api.dto.Session;
import com.zjx.youchat.api.dto.UserContact;
import com.zjx.youchat.ws.constant.ExceptionConstant;
import com.zjx.youchat.ws.constant.UserConstant;
import com.zjx.youchat.ws.domain.dto.UserInfoDTO;
import com.zjx.youchat.ws.domain.dto.WSPackage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@ChannelHandler.Sharable
@Component
public class WSChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final StringRedisTemplate redisTemplate;

    private final ChannelManager channelManager;

    private final ChatClient chatClient;

    // 用户鉴权
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
            String userInfoStr = redisTemplate.opsForValue().get(UserConstant.TOKEN_PREFIX + value);
            if (userInfoStr == null) {
                return null;
            }
            return JSON.parseObject(userInfoStr, UserInfoDTO.class);
        }
        return null;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 注销退出用户的websocket链接
        channelManager.cancelChannel(ctx.channel());

        AttributeKey<String> attributeKey = AttributeKey.valueOf("userId");
        String userId = ctx.channel().attr(attributeKey).get();
        log.info("用户：{} 下线了", userId);
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
        String email = userInfo.getEmail();
        Channel channel = ctx.channel();

        // 将userId绑定到Channel上
        AttributeKey<String> attributeKey = null;
        if (AttributeKey.exists("userId")) {
            attributeKey = AttributeKey.valueOf("userId");
        } else {
            attributeKey = AttributeKey.newInstance("userId");
        }
        channel.attr(attributeKey).setIfAbsent(userId);
        if (AttributeKey.exists("email")) {
            attributeKey = AttributeKey.valueOf("email");
        } else {
            attributeKey = AttributeKey.newInstance("email");
        }
        channel.attr(attributeKey).setIfAbsent(email);

        // 查询用户信息
        WSInitDTO wsInitDTO = new WSInitDTO();
        PersonalInfoDTO personalInfo = chatClient.getPersonalInfo(userId, email);
        List<UserContact> userContacts = chatClient.getMyFriend(userId, email);
        List<GroupContact> groupContacts = chatClient.getMyGroup(userId, email);
        List<Session> sessions = chatClient.getMySession(userId, email);
        List<Message> messages = chatClient.getMyMessage(userId, email);
        wsInitDTO.setPersonalInfo(personalInfo);
        wsInitDTO.setUserContacts(userContacts);
        wsInitDTO.setGroupContacts(groupContacts);
        wsInitDTO.setSessions(sessions);
        wsInitDTO.setMessages(messages);
        WSPackage<WSInitDTO> wsPackage = new WSPackage<>();
        wsPackage.setType(0);
        wsPackage.setReceiverId(userId);
        wsPackage.setData(wsInitDTO);
        channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(wsPackage)));

        // 注册Channel
        channelManager.registerChannel(channel);

        log.info("用户：{} 上线了", userId);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                TextWebSocketFrame textWebSocketFrame) throws Exception {
        // 获取发送WebSocket包的用户id
        Attribute<String> attribute = channelHandlerContext.channel()
                .attr(AttributeKey.valueOf("userId"));
        String userId = attribute.get();

        // 记录日志
        log.info("WS服务器收到WS包 {}: {}", userId, textWebSocketFrame.text());
    }
}
