package com.zjx.youchat.websocket;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class ChannelService {
    private ConcurrentHashMap<String, Channel> map;

    public ChannelService() {
        map = new ConcurrentHashMap<>();
    }
}
