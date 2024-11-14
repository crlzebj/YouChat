package com.zjx.youchat.controller;

import com.wf.captcha.ArithmeticCaptcha;
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
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
		System.out.println(captcha.text());
		return captcha.toBase64();
	}
}
