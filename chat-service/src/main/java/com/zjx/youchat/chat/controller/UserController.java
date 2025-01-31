package com.zjx.youchat.chat.controller;

import com.zjx.youchat.chat.domain.dto.UserLoginDTO;
import com.zjx.youchat.chat.domain.dto.UserRegisterDTO;
import com.zjx.youchat.chat.domain.vo.CaptchaVO;
import com.zjx.youchat.chat.domain.dto.PersonalInfoDTO;
import com.zjx.youchat.chat.domain.vo.ResponseVO;
import com.zjx.youchat.chat.domain.vo.UserLoginVO;
import com.zjx.youchat.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
	private final UserService userService;

	@GetMapping("/captcha")
	public ResponseVO<CaptchaVO> getCaptcha() {
		return ResponseVO.success(userService.getCaptcha());
	}

	@PostMapping("/register")
	public ResponseVO register(@RequestBody UserRegisterDTO userRegisterDTO) {
		userService.register(userRegisterDTO);
		return ResponseVO.success();
	}

	@PostMapping("/login")
	public ResponseVO<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
		UserLoginVO userLoginVO = new UserLoginVO();
		userLoginVO.setToken(userService.login(userLoginDTO));
		return ResponseVO.success(userLoginVO);
	}

	@PostMapping("/logout")
	public ResponseVO logout() {
		userService.logout();
		return ResponseVO.success();
	}

	@GetMapping("/me")
	public PersonalInfoDTO getPersonalInfo() {
		return userService.getPersonalInfo();
	}
}
