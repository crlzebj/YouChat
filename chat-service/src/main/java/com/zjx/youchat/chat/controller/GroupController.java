package com.zjx.youchat.chat.controller;

import com.zjx.youchat.chat.domain.dto.GroupRegisterDTO;
import com.zjx.youchat.chat.domain.vo.ResponseVO;
import com.zjx.youchat.chat.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
public class GroupController {
	@Autowired
	private GroupService groupService;

	@PostMapping("/register")
	public ResponseVO register(GroupRegisterDTO groupRegisterDTO) {
		groupService.register(groupRegisterDTO);
		return ResponseVO.success();
	}
}
