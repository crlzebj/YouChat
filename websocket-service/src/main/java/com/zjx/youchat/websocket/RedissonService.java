package com.zjx.youchat.websocket;

import com.zjx.youchat.websocket.domain.dto.WebSocketPackage;
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
    private ChannelManager channelManager;

    @PostConstruct
    public void subscribe() {
        RTopic topic = redissonClient.getTopic(TOPIC_STRING);
        topic.addListener(WebSocketPackage.class, (charSequence, webSocketPackage) -> {
            channelManager.sendWebSocketPackageDTO(webSocketPackage);
        });
    }

    public void publish(WebSocketPackage websocketPackage) {
        RTopic topic = redissonClient.getTopic(TOPIC_STRING);
        topic.publish(websocketPackage);
    }
}
