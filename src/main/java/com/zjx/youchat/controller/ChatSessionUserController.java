package com.zjx.youchat.controller;

import com.zjx.youchat.service.ChatSessionUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatSessionUsers")
public class ChatSessionUserController {
	@Autowired
	private ChatSessionUserService chatSessionUserService;
}
