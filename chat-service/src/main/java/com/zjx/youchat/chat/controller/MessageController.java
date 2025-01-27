package com.zjx.youchat.chat.controller;

import com.zjx.youchat.chat.domain.dto.MessageSendDTO;
import com.zjx.youchat.chat.domain.po.Message;
import com.zjx.youchat.chat.domain.vo.ResponseVO;
import com.zjx.youchat.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {
	private final MessageService messageService;

	@GetMapping("/myMessage")
	public List<Message> getMyMessage() {
		return messageService.getMyMessage();
	}

	@PostMapping("/send")
	public ResponseVO send(@RequestBody MessageSendDTO messageSendDTO) {
		messageService.send(messageSendDTO);
		return ResponseVO.success();
	}
}
