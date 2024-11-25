package com.zjx.youchat.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.wf.captcha.ArithmeticCaptcha;
import com.zjx.youchat.constant.UserConstant;
import com.zjx.youchat.exception.BusinessException;
import com.zjx.youchat.mapper.UserMapper;
import com.zjx.youchat.pojo.dto.UserLoginDTO;
import com.zjx.youchat.pojo.dto.UserRegisterDTO;
import com.zjx.youchat.pojo.po.User;
import com.zjx.youchat.pojo.vo.CaptchaVO;
import com.zjx.youchat.pojo.vo.PageVO;

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
	public void insert(User user) {
		userMapper.insert(user);
	}

	@Override
	public List<User> select() {
		return userMapper.select(new User());
	}

	@Override
	public List<User> select(User user) {
		return userMapper.select(user);
	}

	@Override
	public Integer count() {
		return userMapper.count(new User());
	}

	@Override
	public Integer count(User user) {
		return userMapper.count(user);
	}

	@Override
	public PageVO<User> selectPage(Integer pageSize, Integer pageNum) {
		PageVO<User> pageVO = new PageVO<User>();
		pageVO.setTotalSize(count());
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count() + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(userMapper.selectPage(new User(), (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public PageVO<User> selectPage(User user, Integer pageSize, Integer pageNum) {
		PageVO<User> pageVO = new PageVO<User>();
		pageVO.setTotalSize(count(user));
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count(user) + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(userMapper.selectPage(user, (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}


	@Override
	public void updateById(String id, User user) {
		userMapper.updateById(id, user);
	}

	@Override
	public void deleteById(String id) {
		userMapper.deleteById(id);
	}

	@Override
	public User selectById(String id) {
		return userMapper.selectById(id);
	}

	@Override
	public void updateByEmail(String email, User user) {
		userMapper.updateByEmail(email, user);
	}

	@Override
	public void deleteByEmail(String email) {
		userMapper.deleteByEmail(email);
	}

	@Override
	public User selectByEmail(String email) {
		return userMapper.selectByEmail(email);
	}

	@Override
	public CaptchaVO getCaptcha() {
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
		String uuid = UUID.randomUUID().toString();

		// 在redis中存入随机的验证码key以及验证码的正确结果
		redisTemplate.opsForValue().set(UserConstant.CAPTCHA_PREFIX + uuid, captcha.text(), Duration.ofMinutes(5));

		// 将随机的验证码key以及验证码图片的base64编码返回
		CaptchaVO captchaVO = new CaptchaVO();
		captchaVO.setUuid(UserConstant.CAPTCHA_PREFIX + uuid);
		captchaVO.setCaptcha(captcha.toBase64());

		return captchaVO;
	}

	@Override
	public void register(UserRegisterDTO userRegisterDTO) {
		// 校验验证码
		if (userRegisterDTO.getCaptchaKey() == null ||
				!userRegisterDTO.getCaptchaValue().equals(redisTemplate.opsForValue().get(userRegisterDTO.getCaptchaKey()))) {
			throw new BusinessException("验证码不正确");
		}

		// 校验邮箱是否可用
		if (selectByEmail(userRegisterDTO.getEmail()) != null) {
			throw new BusinessException("邮箱已使用");
		}

		// 为新用户创建id
		String id = "U" + RandomStringUtils.random(7, false, true);
		while (selectById(id) != null) {
			id = "U" + RandomStringUtils.random(7, false, true);
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
		insert(newUser);
	}

	@Override
	public String login(UserLoginDTO userLoginDTO) {
		// 校验验证码
		if (userLoginDTO.getCaptchaKey() == null ||
				!userLoginDTO.getCaptchaValue().equals(redisTemplate.opsForValue().get(userLoginDTO.getCaptchaKey()))) {
			throw new BusinessException("验证码不正确");
		}

		// 校验用户名以及密码
		User user = userMapper.selectByEmail(userLoginDTO.getEmail());
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
