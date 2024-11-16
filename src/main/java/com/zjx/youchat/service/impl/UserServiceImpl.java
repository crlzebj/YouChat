package com.zjx.youchat.service.impl;

import com.wf.captcha.ArithmeticCaptcha;
import com.zjx.youchat.exception.BusinessException;
import com.zjx.youchat.mapper.UserMapper;
import com.zjx.youchat.pojo.dto.UserRegisterDTO;
import com.zjx.youchat.pojo.po.User;
import com.zjx.youchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private StringRedisTemplate redisTemplate;

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
	public Map<String, String> getCaptcha() {
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
		String uuid = UUID.randomUUID().toString();
		redisTemplate.opsForValue().set("youchat:captcha:" + uuid, captcha.text(), Duration.ofMinutes(5));
		Map<String, String> response = new HashMap<>();
		response.put("youchat:captcha:" + uuid, captcha.toBase64());
		return response;
	}

	@Override
	public void register(UserRegisterDTO userRegisterDTO) {
		if (!userRegisterDTO.getCaptchaValue().equals(redisTemplate.opsForValue().get(userRegisterDTO.getCaptchaKey()))) {
			throw new BusinessException("验证码不正确");
		}
		User user = selectUserByEmail(userRegisterDTO.getEmail());
		if (user != null) {
			throw new BusinessException("邮箱已使用");
		}
		Random random = new Random();
		String id = String.valueOf(random.nextInt()).substring(0, 8);
		user = selectUserById(id);
		if (user != null) {
			return;
		}
		User newUser = new User();
		newUser.setId(id);
		newUser.setEmail(userRegisterDTO.getEmail());
		newUser.setPassword(userRegisterDTO.getPassword());
		newUser.setNickname(userRegisterDTO.getNickname());
		insertUser(newUser);
	}
}
