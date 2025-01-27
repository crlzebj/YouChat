package com.zjx.youchat.ws.controller;

import com.zjx.youchat.ws.domain.dto.WSPackage;
import com.zjx.youchat.ws.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/msg")
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/user")
    public void sendMsgToUser(@RequestBody WSPackage wsPackage) {
        messageService.sendMsgToUser(wsPackage);
    }

    @PostMapping("/group")
    public void sendMsgToGroup(@RequestBody WSPackage wsPackage) {
        messageService.sendMsgToGroup(wsPackage);
    }
}
