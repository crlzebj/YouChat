package com.zjx.youchat.websocket.service;

import com.alibaba.fastjson.JSON;
import com.zjx.youchat.constant.UserConstant;
import com.zjx.youchat.constant.WebsocketPackageConstant;
import com.zjx.youchat.exception.BusinessException;
import com.zjx.youchat.pojo.dto.UserInfoDTO;
import com.zjx.youchat.pojo.dto.WebsocketPackageDTO;
import com.zjx.youchat.pojo.po.Contact;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

@Component
public class ChannelService {
    private final ConcurrentHashMap<String, Channel> userIdToChannelMap;
    private final ConcurrentHashMap<String, ChannelGroup> groupIdToChannelGroupMap;

    @Autowired
    StringRedisTemplate redisTemplate;

    public ChannelService() {
        userIdToChannelMap = new ConcurrentHashMap<>();
        groupIdToChannelGroupMap = new ConcurrentHashMap<>();
    }

    public void registerChannel(String userId, Channel channel) {
        // 将用户id绑定到channel上
        String channelId = channel.id().toString();
        AttributeKey<String> key = null;
        if (AttributeKey.exists(userId)) {
            key = AttributeKey.valueOf(channelId);
        } else {
            key = AttributeKey.newInstance(channelId);
        }
        channel.attr(key).set(userId);

        // 将用户channel加入map，以供给该用户发消息时使用
        userIdToChannelMap.put(userId, channel);
        String userInitialInfoJSON = redisTemplate.opsForValue().get(UserConstant.USER_INITIAL_INFO_PREFIX + userId);

        // 将用户channel加入群组的ChannelGroup，给该群组发消息时会发给群组中的所有channel
        UserInfoDTO userInfoDTO = JSON.parseObject(userInitialInfoJSON, UserInfoDTO.class);
        if (userInfoDTO == null) {
            throw new BusinessException("JSON解析出错");
        }
        List<Contact> chatGroupContacts = userInfoDTO.getChatGroupContacts();
        for (Contact chatGroupContact : chatGroupContacts) {
            if (groupIdToChannelGroupMap.get(chatGroupContact.getAccepterId()) == null) {
                groupIdToChannelGroupMap.put(chatGroupContact.getAccepterId(),
                        new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
            }
            groupIdToChannelGroupMap.get(chatGroupContact.getAccepterId()).add(channel);
        }
        WebsocketPackageDTO websocketPackageDTO = new WebsocketPackageDTO();
        websocketPackageDTO.setType(WebsocketPackageConstant.USER_INFO_TYPE);
        websocketPackageDTO.setReceiverId(userId);
        websocketPackageDTO.setData(userInfoDTO);
        sendInfo(websocketPackageDTO);
    }

    public void sendInfo(WebsocketPackageDTO websocketPackageDTO) {
        String receiverId = websocketPackageDTO.getReceiverId();
        if (!userIdToChannelMap.containsKey(receiverId)) {
            return;
        }
        userIdToChannelMap.get(receiverId)
                .writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(websocketPackageDTO)));
    }
}
