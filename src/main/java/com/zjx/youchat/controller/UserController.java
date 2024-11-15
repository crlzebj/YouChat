package com.zjx.youchat.controller;

import com.zjx.youchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("captcha")
	public String getCaptcha() {
		return userService.getCaptcha();
	}
}
