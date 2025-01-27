package com.zjx.youchat.chat.controller;

import com.zjx.youchat.chat.domain.po.Session;
import com.zjx.youchat.chat.domain.vo.ResponseVO;
import com.zjx.youchat.chat.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/session")
public class SessionController {
    private final SessionService sessionService;

    @GetMapping("/mySession")
    public List<Session> getMySession() {
        return sessionService.getMySession();
    }
}
