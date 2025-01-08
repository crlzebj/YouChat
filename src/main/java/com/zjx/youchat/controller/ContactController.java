package com.zjx.youchat.controller;

import com.zjx.youchat.pojo.po.Group;
import com.zjx.youchat.pojo.vo.ResponseVO;
import com.zjx.youchat.pojo.vo.UserViewVO;
import com.zjx.youchat.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
public class ContactController {
	@Autowired
	private ContactService contactService;

	@GetMapping("/viewUser")
	public ResponseVO<UserViewVO> viewUser(@RequestParam String userId) {
		return ResponseVO.success(contactService.viewUser(userId));
	}

	@GetMapping("/viewGroup")
	public ResponseVO<Group> viewGroup(@RequestParam String groupId) {
		return ResponseVO.success(contactService.viewGroup(groupId));
	}

	@GetMapping("/accept")
	public ResponseVO accept(@RequestParam Long contactId) {
		contactService.accept(contactId);
		return ResponseVO.success();
	}
}
