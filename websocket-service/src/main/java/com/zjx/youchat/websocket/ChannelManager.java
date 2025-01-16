package com.zjx.youchat.websocket;

import com.alibaba.fastjson.JSON;
import com.zjx.youchat.websocket.constant.ExceptionConstant;
import com.zjx.youchat.websocket.constant.enums.WebSocketPackageEnum;
import com.zjx.youchat.websocket.domain.dto.WebSocketPackage;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Channel管理器
 */
@Slf4j
@Component
public class ChannelManager {
    // 存放userId和Channel的对应关系
    private final ConcurrentHashMap<String, Channel> userIdToChannel;

    // 存放groupId和ChannelGroup的对应关系
    private final ConcurrentHashMap<String, ChannelGroup> groupIdToChannelGroup;

    public ChannelManager() {
        userIdToChannel = new ConcurrentHashMap<>();
        groupIdToChannelGroup = new ConcurrentHashMap<>();
    }

    /**
     * 注册Channel
     * @param userId
     * @param channel
     */
    public void registerChannel(String userId, Channel channel) {
        // 将Channel加入map，以供该用户收发消息使用
        userIdToChannel.put(userId, channel);

        // TODO发送websocket消息
        // 查询用户的所有好友、群组、会话、申请以及消息
        // 好友
//        List<UserContact> userContacts = contactMapper.queryUserContactByInitiatorId(userId);
//        userContacts.addAll(contactMapper.queryUserContactByAccepterId(userId));
//
//        // 群组
//        List<GroupContact> groupContacts1 = contactMapper.queryGroupContactByInitiatorId(userId);
//        List<GroupContact> groupContacts2 = contactMapper.queryGroupContactByAccepterId(userId);
//
//        // 将用户Channel加入群组的ChannelGroup，给该群组发消息时会发给群组中的所有用户
//        for (GroupContact groupContact : groupContacts1) {
//            if (groupIdToChannelGroup.get(groupContact.getAccepterId()) == null) {
//                groupIdToChannelGroup.put(groupContact.getAccepterId(),
//                        new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
//            }
//            groupIdToChannelGroup.get(groupContact.getAccepterId()).add(channel);
//        }
//        for (GroupContact groupContact : groupContacts2) {
//            if (groupIdToChannelGroup.get(groupContact.getInitiatorId()) == null) {
//                groupIdToChannelGroup.put(groupContact.getInitiatorId(),
//                        new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
//            }
//            groupIdToChannelGroup.get(groupContact.getInitiatorId()).add(channel);
//        }
//        groupContacts1.addAll(groupContacts2);
    }

    /**
     * 注销Channel
     * @param userId
     */
    public void cancelChannel(String userId) {
        // ChannelGroup会自动移除其中已经关闭的Channel
        Channel channel = userIdToChannel.get(userId);
        if (channel == null) {
            return;
        }
        channel.close();
        userIdToChannel.remove(userId);
    }

    /**
     * 发送WebSocketPackageDTO
     * @param webSocketPackage
     */
    public void sendWebSocketPackageDTO(WebSocketPackage webSocketPackage) {
        //数据库逻辑
        // TODO消息队列异步修改数据库
//        switch (WebSocketPackageEnum.getInstanceByValue(webSocketPackage.getType())) {
//            case LOGIN_INIT:
//                break;
//            case MESSAGE:
//                Message message = JSON.parseObject(JSON.toJSONString(webSocketPackage.getData()), Message.class);
//                messageService.send(message);
//                break;
//            case REQUEST:
//                break;
//            case REQUEST_DONE:
//                break;
//            case ACCOUNT_BANNED:
//                break;
//            case GROUP_BANNED:
//                break;
//            default:
//                log.info("异常信息：{}", ExceptionConstant.WEBSOCKET_PACKAGE_FORMAT_ERROR);
//                return;
//        }

        String receiverId = webSocketPackage.getReceiverId();
        if (!userIdToChannel.containsKey(receiverId)) {
            return;
        }
        userIdToChannel.get(receiverId)
                .writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(webSocketPackage)));
        if (WebSocketPackageEnum.getInstanceByValue(webSocketPackage.getType())
                == WebSocketPackageEnum.ACCOUNT_BANNED) {
            userIdToChannel.get(receiverId).close();
            userIdToChannel.remove(receiverId);
        }
    }
}
