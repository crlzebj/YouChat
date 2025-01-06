package com.zjx.youchat.websocket;

import com.alibaba.fastjson.JSON;
import com.zjx.youchat.constant.enums.WebSocketPackageEnum;
import com.zjx.youchat.pojo.dto.WebSocketPackage;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * Channel管理器
 */
@Component
public class ChannelManager {
    // 存放userId和Channel的对应关系
    private final ConcurrentHashMap<String, Channel> userIdToChannelMap;
    // 存放groupId和ChannelGroup的对应关系
    private final ConcurrentHashMap<String, ChannelGroup> groupIdToChannelGroupMap;

    @Autowired
    StringRedisTemplate redisTemplate;

    public ChannelManager() {
        userIdToChannelMap = new ConcurrentHashMap<>();
        groupIdToChannelGroupMap = new ConcurrentHashMap<>();
    }

    /**
     * 注册Channel
     * @param userId
     * @param channel
     */
    public void registerChannel(String userId, Channel channel) {
        // 将userId绑定到对应Channel上
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
//        String userInitialInfoJSON = redisTemplate.opsForValue().get(UserConstant.USER_INITIAL_INFO_PREFIX + userId);
//
//        // 将用户channel加入群组的ChannelGroup，给该群组发消息时会发给群组中的所有channel
//        UserInfoDTO userInfoDTO = JSON.parseObject(userInitialInfoJSON, UserInfoDTO.class);
//        if (userInfoDTO == null) {
//            throw new BusinessException("JSON解析出错");
//        }
//        List<Contact> chatGroupContacts = userInfoDTO.getChatGroupContacts();
//        for (Contact chatGroupContact : chatGroupContacts) {
//            if (groupIdToChannelGroupMap.get(chatGroupContact.getAccepterId()) == null) {
//                groupIdToChannelGroupMap.put(chatGroupContact.getAccepterId(),
//                        new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
//            }
//            groupIdToChannelGroupMap.get(chatGroupContact.getAccepterId()).add(channel);
//        }
//        WebSocketPackage websocketPackage = new WebSocketPackage();
//        websocketPackage.setType(WebsocketPackageConstant.USER_INFO_TYPE);
//        websocketPackage.setReceiverId(userId);
//        websocketPackage.setData(userInfoDTO);
//        sendWebSocketPackageDTO(websocketPackage);
    }

    /**
     * 注销Channel
     * @param userId
     */
    public void cancelChannel(String userId) {
        // ChannelGroup会自动移除其中已经关闭的Channel
        Channel channel = userIdToChannelMap.get(userId);
        if (channel == null) {
            return;
        }
        channel.close();
        userIdToChannelMap.remove(userId);
    }

    /**
     * 发送WebSocketPackageDTO
     * @param websocketPackage
     */
    public void sendWebSocketPackageDTO(WebSocketPackage websocketPackage) {
        String receiverId = websocketPackage.getReceiverId();
        if (!userIdToChannelMap.containsKey(receiverId)) {
            return;
        }
        userIdToChannelMap.get(receiverId)
                .writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(websocketPackage)));
        if (WebSocketPackageEnum.getInstanceByValue(websocketPackage.getType())
                == WebSocketPackageEnum.ACCOUNT_BANNED) {
            userIdToChannelMap.get(receiverId).close();
            userIdToChannelMap.remove(receiverId);
        }
    }
}
