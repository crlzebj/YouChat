package com.zjx.youchat.ws.service;

import com.zjx.youchat.ws.domain.dto.WSPackage;
import com.zjx.youchat.ws.wsServer.ChannelManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final ChannelManager channelManager;

    public void sendMsgToUser(WSPackage wsPackage) {
        channelManager.sendWSPackageToUser(wsPackage);
    }

    public void sendMsgToGroup(WSPackage wsPackage) {
        channelManager.sendWSPackageToGroup(wsPackage);
    }
}
