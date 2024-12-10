package com.zjx.youchat.controller;

import com.zjx.youchat.service.ChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatSessions")
public class ChatSessionController {
	@Autowired
	private ChatSessionService chatSessionService;
}
