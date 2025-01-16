package com.zjx.youchat.chat.controller;

import com.zjx.youchat.chat.domain.dto.ContactApplyAddDTO;
import com.zjx.youchat.chat.domain.vo.GroupQueryVO;
import com.zjx.youchat.chat.domain.vo.ResponseVO;
import com.zjx.youchat.chat.domain.vo.UserQueryVO;
import com.zjx.youchat.chat.service.ContactApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contactApply")
public class ContactApplyController {
	@Autowired
	private ContactApplyService contactApplyService;

	@GetMapping("/queryUser")
	public ResponseVO<UserQueryVO> queryUser(@RequestParam String userId) {
		return ResponseVO.success(contactApplyService.queryUser(userId));
	}

	@GetMapping("/queryGroup")
	public ResponseVO<GroupQueryVO> queryGroup(@RequestParam String groupId) {
		return ResponseVO.success(contactApplyService.queryGroup(groupId));
	}

	@PostMapping("/add")
	public ResponseVO add(@RequestBody ContactApplyAddDTO contactApplyAddDTO) {
		contactApplyService.add(contactApplyAddDTO);
		return ResponseVO.success();
	}
}
