package com.zjx.youchat.chat.controller;

import com.zjx.youchat.chat.domain.dto.MessageSendDTO;
import com.zjx.youchat.chat.domain.vo.ResponseVO;
import com.zjx.youchat.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {
	private final MessageService messageService;

	@PostMapping("/send")
	public ResponseVO send(@RequestBody MessageSendDTO messageSendDTO) {
		messageService.send(messageSendDTO);
		return ResponseVO.success();
	}
}
