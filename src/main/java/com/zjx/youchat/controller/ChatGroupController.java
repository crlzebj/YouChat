package com.zjx.youchat.controller;

import com.zjx.youchat.service.ChatGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatGroups")
public class ChatGroupController {
	@Autowired
	private ChatGroupService chatGroupService;
}
