package com.zjx.youchat.controller;

import com.zjx.youchat.pojo.dto.UserRegisterDTO;
import com.zjx.youchat.pojo.vo.ResponseVO;
import com.zjx.youchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("captcha")
	public ResponseVO<Map<String, String>> getCaptcha() {
		Map<String, String> captcha = userService.getCaptcha();
		return ResponseVO.success(captcha);
	}

	@PostMapping("register")
	public ResponseVO register(@RequestBody UserRegisterDTO userRegisterDTO) {
		userService.register(userRegisterDTO);
		return ResponseVO.success();
	}
}
