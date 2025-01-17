package com.zjx.youchat.chat.controller;

import com.zjx.youchat.chat.domain.dto.ContactApplyDTO;
import com.zjx.youchat.chat.domain.vo.GroupQueryVO;
import com.zjx.youchat.chat.domain.vo.ResponseVO;
import com.zjx.youchat.chat.domain.vo.UserQueryVO;
import com.zjx.youchat.chat.service.ContactApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/contactApply")
public class ContactApplyController {
	private final ContactApplyService contactApplyService;

	@GetMapping("/queryUser")
	public ResponseVO<UserQueryVO> queryUser(@RequestParam String userId) {
		return ResponseVO.success(contactApplyService.queryUser(userId));
	}

	@GetMapping("/queryGroup")
	public ResponseVO<GroupQueryVO> queryGroup(@RequestParam String groupId) {
		return ResponseVO.success(contactApplyService.queryGroup(groupId));
	}

	@PostMapping("/apply")
	public ResponseVO apply(@RequestBody ContactApplyDTO contactApplyDTO) {
		contactApplyService.apply(contactApplyDTO);
		return ResponseVO.success();
	}

	@GetMapping("/accept")
	public ResponseVO accept(@RequestParam Long id) {
		contactApplyService.accept(id);
		return ResponseVO.success();
	}
}
