package com.zjx.youchat.controller;

import com.zjx.youchat.pojo.dto.UserLoginDTO;
import com.zjx.youchat.pojo.dto.UserRegisterDTO;
import com.zjx.youchat.pojo.vo.ResponseVO;
import com.zjx.youchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("users")
public class UserController {
	@Autowired
	private UserService userService;

	/**
	 * 获取验证码
	 * @return
	 */
	@GetMapping("captcha")
	public ResponseVO<Map<String, String>> getCaptcha() {
		Map<String, String> captcha = userService.getCaptcha();
		return ResponseVO.success(captcha);
	}

	/**
	 * 注册账号
	 * @param userRegisterDTO
	 * @return
	 */
	@PostMapping("register")
	public ResponseVO register(@RequestBody UserRegisterDTO userRegisterDTO) {
		userService.register(userRegisterDTO);
		return ResponseVO.success();
	}

	/**
	 * 用户登录
	 * @param userLoginDTO
	 * @return
	 */
	@PostMapping("login")
	public ResponseVO login(@RequestBody UserLoginDTO userLoginDTO) {
		String jwt = userService.login(userLoginDTO);
		return ResponseVO.success(jwt);
	}
}
