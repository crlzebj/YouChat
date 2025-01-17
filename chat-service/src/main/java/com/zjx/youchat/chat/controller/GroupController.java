package com.zjx.youchat.chat.controller;

import com.zjx.youchat.chat.domain.dto.GroupRegisterDTO;
import com.zjx.youchat.chat.domain.vo.ResponseVO;
import com.zjx.youchat.chat.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/group")
public class GroupController {
	private final GroupService groupService;

	@PostMapping("/register")
	public ResponseVO register(GroupRegisterDTO groupRegisterDTO) {
		groupService.register(groupRegisterDTO);
		return ResponseVO.success();
	}
}
