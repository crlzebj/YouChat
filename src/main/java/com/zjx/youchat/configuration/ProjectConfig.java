package com.zjx.youchat.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ProjectConfig {
    @Value("${you-chat.websocket-port}")
    private Integer websocketPort;

    @Value("${you-chat.server.data-path}")
    private String serverDataPath;

    @Value("${you-chat.client.data-path}")
    private String clientDataPath;
}
