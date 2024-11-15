package com.zjx.youchat.service.impl;

import com.wf.captcha.ArithmeticCaptcha;
import com.zjx.youchat.mapper.UserMapper;
import com.zjx.youchat.pojo.entity.User;
import com.zjx.youchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public void insertUser(User user) {
		userMapper.insertUser(user);
	}

	@Override
	public List<User> selectUser() {
		return userMapper.selectUser();
	}

	@Override
	public void updateUserById(String id, User user) {
		userMapper.updateUserById(id, user);
	}

	@Override
	public void deleteUserById(String id) {
		userMapper.deleteUserById(id);
	}

	@Override
	public User selectUserById(String id) {
		return userMapper.selectUserById(id);
	}

	@Override
	public void updateUserByEmail(String email, User user) {
		userMapper.updateUserByEmail(email, user);
	}

	@Override
	public void deleteUserByEmail(String email) {
		userMapper.deleteUserByEmail(email);
	}

	@Override
	public User selectUserByEmail(String email) {
		return userMapper.selectUserByEmail(email);
	}

	@Override
	public String getCaptcha() {
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
		System.out.println(captcha.text());
		redisTemplate.opsForValue().set("captcha", captcha.text());
		return captcha.toBase64();
	}
}
