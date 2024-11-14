package com.zjx.youchat.controller;

import com.zjx.youchat.pojo.entity.User;
import com.zjx.youchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping
	public List<User> selectUser() {
		return userService.selectUser();
	}
}
