package com.zjx.youchat.controller;

import com.zjx.youchat.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatMessages")
public class ChatMessageController {
	@Autowired
	private ChatMessageService chatMessageService;
}
