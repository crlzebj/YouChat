package com.zjx.youchat.chat.controller;

import com.zjx.youchat.chat.domain.po.Group;
import com.zjx.youchat.chat.domain.po.GroupContact;
import com.zjx.youchat.chat.domain.vo.ResponseVO;
import com.zjx.youchat.chat.domain.po.UserContact;
import com.zjx.youchat.chat.domain.vo.UserViewVO;
import com.zjx.youchat.chat.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/contact")
public class ContactController {
	private final ContactService contactService;

	@GetMapping("/myFriend")
	public List<UserContact> getMyFriend() {
		return contactService.getMyFriend();
	}

	@GetMapping("/myGroup")
	public List<GroupContact> getMyGroup() {
		return contactService.getMyGroup();
	}

	@GetMapping("/viewUser")
	public ResponseVO<UserViewVO> viewUser(@RequestParam String userId) {
		return ResponseVO.success(contactService.viewUser(userId));
	}

	@GetMapping("/viewGroup")
	public ResponseVO<Group> viewGroup(@RequestParam String groupId) {
		return ResponseVO.success(contactService.viewGroup(groupId));
	}
}
