package com.zjx.youchat.websocket.service;

import com.zjx.youchat.constant.WebsocketPackageConstant;
import com.zjx.youchat.pojo.dto.WebsocketPackageDTO;
import com.zjx.youchat.pojo.po.Message;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RedissonService {
    private static final String TOPIC_STRING;

    static {
        TOPIC_STRING = "YOU_CHAT_TOPIC";
    }

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ChannelService channelService;

    @PostConstruct
    public void subscribe() {
        RTopic topic = redissonClient.getTopic(TOPIC_STRING);
        topic.addListener(WebsocketPackageDTO.class, (charSequence, websocketPackageDTO) -> {
            channelService.sendInfo(websocketPackageDTO);
        });
    }

    public void publish(WebsocketPackageDTO websocketPackageDTO) {
        RTopic topic = redissonClient.getTopic(TOPIC_STRING);
        topic.publish(websocketPackageDTO);
    }
}
