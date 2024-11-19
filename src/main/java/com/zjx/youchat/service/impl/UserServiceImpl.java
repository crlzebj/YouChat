package com.zjx.youchat.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.wf.captcha.ArithmeticCaptcha;
import com.zjx.youchat.constant.UserConstant;
import com.zjx.youchat.exception.BusinessException;
import com.zjx.youchat.mapper.UserMapper;
import com.zjx.youchat.pojo.dto.UserLoginDTO;
import com.zjx.youchat.pojo.dto.UserRegisterDTO;
import com.zjx.youchat.pojo.po.User;
import com.zjx.youchat.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
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

		// 在redis中存入随机的验证码key以及验证码的正确结果
		redisTemplate.opsForValue().set(UserConstant.CAPTCHA_PREFIX + uuid, captcha.text(), Duration.ofMinutes(5));

		// 将随机的验证码key以及验证码图片的base64编码返回
		Map<String, String> response = new HashMap<>();
		response.put(UserConstant.CAPTCHA_PREFIX + uuid, captcha.toBase64());

		return response;
	}

	@Override
	public void register(UserRegisterDTO userRegisterDTO) {
		// 校验验证码
		if (userRegisterDTO.getCaptchaKey() == null ||
				!userRegisterDTO.getCaptchaValue().equals(redisTemplate.opsForValue().get(userRegisterDTO.getCaptchaKey()))) {
			throw new BusinessException("验证码不正确");
		}

		// 校验邮箱是否可用
		if (selectUserByEmail(userRegisterDTO.getEmail()) != null) {
			throw new BusinessException("邮箱已使用");
		}

		// 为新用户创建id
		String id = RandomStringUtils.random(8, false, true);
		while (selectUserById(id) != null) {
			id = RandomStringUtils.random(8, false, true);
		}

		// 创建新用户
		User newUser = new User();
		newUser.setId(id);
		newUser.setEmail(userRegisterDTO.getEmail());
		// 对用户密码进行SHA256加密
		String password = DigestUtil.sha256Hex(userRegisterDTO.getPassword().getBytes());
		newUser.setPassword(password);
		newUser.setNickname(userRegisterDTO.getNickname());
		newUser.setCreateTime(LocalDateTime.now());
		insertUser(newUser);
	}

	@Override
	public String login(UserLoginDTO userLoginDTO) {
		// 校验验证码
		if (userLoginDTO.getCaptchaKey() == null ||
				!userLoginDTO.getCaptchaValue().equals(redisTemplate.opsForValue().get(userLoginDTO.getCaptchaKey()))) {
			throw new BusinessException("验证码不正确");
		}

		// 校验用户名以及密码
		User user = userMapper.selectUserByEmail(userLoginDTO.getEmail());
		// 将传入的密码进行SHA256加密
		String password = DigestUtil.sha256Hex(userLoginDTO.getPassword().getBytes());
		if (user == null || !password.equals(user.getPassword())) {
			throw new BusinessException("错误的用户名或密码");
		}

		// 判断用户是否被封禁

		// 判断用户是否已登录

		// 生成JWT
		Map<String, Object> payload = new HashMap<>();
		payload.put("id", user.getId());
		payload.put("email", user.getEmail());
		payload.put("nickname", user.getNickname());
		String jwt = Jwts.builder().setClaims(payload)
				.signWith(SignatureAlgorithm.HS256, UserConstant.SECRET_KEY)
				.setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
				.compact();

		return jwt;
	}
}
