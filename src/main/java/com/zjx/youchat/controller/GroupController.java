package com.zjx.youchat.controller;

import com.zjx.youchat.pojo.dto.GroupRegisterDTO;
import com.zjx.youchat.pojo.po.Group;
import com.zjx.youchat.pojo.vo.GroupQueryVO;
import com.zjx.youchat.pojo.vo.ResponseVO;
import com.zjx.youchat.service.GroupService;
import com.zjx.youchat.util.ThreadLocalUtil;
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
