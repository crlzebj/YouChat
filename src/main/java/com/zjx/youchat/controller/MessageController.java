package com.zjx.youchat.controller;

import com.zjx.youchat.pojo.po.Message;
import com.zjx.youchat.pojo.vo.ResponseVO;
import com.zjx.youchat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {
	@Autowired
	private MessageService messageService;

	@PostMapping("send")
	public ResponseVO send(@RequestBody Message message) {
		messageService.send(message);
		return ResponseVO.success();
	}
}
