package com.zjx.youchat.ws.wsServer;

import com.alibaba.fastjson.JSON;
import com.zjx.youchat.api.client.ChatClient;
import com.zjx.youchat.api.dto.GroupContact;
import com.zjx.youchat.ws.constant.enums.WSPackageEnum;
import com.zjx.youchat.ws.domain.dto.WSPackage;
import com.zjx.youchat.ws.service.URIService;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Channel管理器
 */
@Slf4j
@Component
public class ChannelManager {
    @Value("${server.port}")
    private int port;

    // 存放userId和Channel的对应关系
    private final ConcurrentHashMap<String, Channel> userIdToChannel;

    // 存放groupId和ChannelGroup的对应关系
    private final ConcurrentHashMap<String, ChannelGroup> groupIdToChannelGroup;

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private URIService uriService;

    public ChannelManager() {
        userIdToChannel = new ConcurrentHashMap<>();
        groupIdToChannelGroup = new ConcurrentHashMap<>();
    }

    /**
     * 注册Channel
     * @param channel
     */
    public void registerChannel(Channel channel) {
        AttributeKey<String> attributeKey = AttributeKey.valueOf("userId");
        String userId = channel.attr(attributeKey).get();
        attributeKey = AttributeKey.valueOf("email");
        String email = channel.attr(attributeKey).get();

        // 将Channel加入map，以供该用户收发消息使用
        userIdToChannel.put(userId, channel);

        // 添加登录用户的uri缓存
        String uri;
        try {
            uri = InetAddress.getLocalHost().getHostAddress() + ":" + port;
        } catch (Exception e) {
            log.info("服务端异常：{}", e.getMessage());
            return;
        }
        uriService.addUser(userId, uri);

        // 将Channel加入ChannelGroup
        List<GroupContact> groupContacts = chatClient.getMyGroup(userId, email);
        for (GroupContact groupContact : groupContacts) {
            String groupId = groupContact.getInitiatorId().compareTo(userId) == 0 ?
                    groupContact.getAccepterId() : groupContact.getInitiatorId();
            if (!groupIdToChannelGroup.containsKey(groupId)) {
                groupIdToChannelGroup.put(groupId, new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
            }
            groupIdToChannelGroup.get(groupId).add(channel);

            // 添加群组的uri缓存
            uriService.addGroup(groupId, uri);
        }
    }

    /**
     * 注销Channel
     * @param channel
     */
    public void cancelChannel(Channel channel) {
        AttributeKey<String> attributeKey = AttributeKey.valueOf("userId");
        String userId = channel.attr(attributeKey).get();
        attributeKey = AttributeKey.valueOf("email");
        String email = channel.attr(attributeKey).get();

        // 删除退出用户的uri缓存
        uriService.deleteUser(userId);

        // 管理群组的uri缓存
        List<GroupContact> groupContacts = chatClient.getMyGroup(userId, email);
        for (GroupContact groupContact : groupContacts) {
            String groupId = groupContact.getInitiatorId().compareTo(userId) == 0 ?
                    groupContact.getAccepterId() : groupContact.getInitiatorId();
            String uri;
            try {
                uri = InetAddress.getLocalHost().getHostAddress() + ":" + port;
            } catch (Exception e) {
                log.info("服务端异常：{}", e.getMessage());
                return;
            }
            uriService.deleteGroup(groupId, uri);
        }

        userIdToChannel.remove(userId);

        // ChannelGroup会自动移除其中已经关闭的Channel
        channel.close();
    }

    /**
     * 发送WSPackage
     * @param WSPackage
     */
    public void sendWSPackageToUser(WSPackage WSPackage) {
        String receiverId = WSPackage.getReceiverId();
        if (!userIdToChannel.containsKey(receiverId)) {
            return;
        }
        userIdToChannel.get(receiverId)
                .writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(WSPackage)));
        if (WSPackageEnum.getInstanceByValue(WSPackage.getType())
                == WSPackageEnum.ACCOUNT_BANNED) {
            userIdToChannel.get(receiverId).close();
            userIdToChannel.remove(receiverId);
        }
    }

    public void sendWSPackageToGroup(WSPackage WSPackage) {
        String receiverId = WSPackage.getReceiverId();
        if (!groupIdToChannelGroup.containsKey(receiverId)) {
            return;
        }
        groupIdToChannelGroup.get(receiverId)
                .writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(WSPackage)));
        if (WSPackageEnum.getInstanceByValue(WSPackage.getType())
                == WSPackageEnum.GROUP_BANNED) {
            groupIdToChannelGroup.remove(receiverId);
        }
    }
}
