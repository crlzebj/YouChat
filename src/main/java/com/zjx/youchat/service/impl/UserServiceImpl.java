package com.zjx.youchat.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.wf.captcha.ArithmeticCaptcha;
import com.zjx.youchat.constant.RobotConstant;
import com.zjx.youchat.constant.UserConstant;
import com.zjx.youchat.exception.BusinessException;
import com.zjx.youchat.mapper.UserMapper;
import com.zjx.youchat.pojo.dto.UserLoginDTO;
import com.zjx.youchat.pojo.dto.UserRegisterDTO;
import com.zjx.youchat.pojo.po.*;
import com.zjx.youchat.pojo.vo.CaptchaVO;
import com.zjx.youchat.pojo.vo.PageVO;

import com.zjx.youchat.service.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private ContactService contactService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private MessageService messageService;

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
		redisTemplate.opsForValue().set(UserConstant.CAPTCHA_PREFIX + uuid,
				captcha.text(), Duration.ofMinutes(1));

		// 将随机的验证码key以及验证码图片的base64编码返回
		CaptchaVO captchaVO = new CaptchaVO();
		captchaVO.setUuid(UserConstant.CAPTCHA_PREFIX + uuid);
		captchaVO.setCaptcha(captcha.toBase64());

		return captchaVO;
	}

	@Override
	@Transactional
	public void register(UserRegisterDTO userRegisterDTO) {
		// 校验验证码
		if (userRegisterDTO.getCaptchaKey() == null) {
			throw new BusinessException("请输入验证码");
		}
		if(!userRegisterDTO.getCaptchaValue().equals(
				redisTemplate.opsForValue().get(userRegisterDTO.getCaptchaKey()))) {
			redisTemplate.delete(userRegisterDTO.getCaptchaKey());
			throw new BusinessException("验证码不正确");
		}
		redisTemplate.delete(userRegisterDTO.getCaptchaKey());

		// 校验邮箱是否可用
		if (selectByEmail(userRegisterDTO.getEmail()) != null) {
			throw new BusinessException("邮箱已使用");
		}

		// 为新用户分配id
		String id = "U" + RandomStringUtils.random(7, false, true);
		while (selectById(id) != null) {
			id = "U" + RandomStringUtils.random(7, false, true);
		}

		// 数据库中插入新用户记录
		User user = new User();
		user.setId(id);
		user.setEmail(userRegisterDTO.getEmail());
		// 对用户密码进行SHA256加密
		String password = DigestUtil.sha256Hex(userRegisterDTO.getPassword().getBytes());
		user.setPassword(password);
		user.setStatus(1);
		user.setPermission(1);
		user.setNickname(userRegisterDTO.getNickname());
		user.setCreateTime(LocalDateTime.now());
		user.setLastLoginTime(LocalDateTime.now());
		user.setLastLogoutTime(LocalDateTime.now());
		insert(user);

		/*
			将聊天机器人加入新用户好友列表
			为机器人和新用户创建会话窗口
			机器人给新用户发送欢迎消息
		 */
		Contact contact = new Contact();
		contact.setInitiatorId(RobotConstant.ROBOT_ID);
		contact.setInitiatorNickname(RobotConstant.ROBOT_NICKNAME);
		contact.setAccepterId(id);
		contact.setAccepterNickname(user.getNickname());
		contact.setType(0);
		contact.setStatus(0);
		contact.setCreateTime(LocalDateTime.now());
		contact.setLastUpdateTime(LocalDateTime.now());
		contactService.insert(contact);

		Session session = new Session();
		session.setInitiatorId(RobotConstant.ROBOT_ID);
		session.setInitiatorNickname(RobotConstant.ROBOT_NICKNAME);
		session.setAccepterId(id);
		session.setAccepterNickname(user.getNickname());
		String sessionId = RobotConstant.ROBOT_ID + id;
		session.setId(DigestUtil.md5Hex(sessionId.getBytes()));
		session.setLastMessage(RobotConstant.HELLO_MESSAGE);
		session.setLastReceiveTime(LocalDateTime.now());
		sessionService.insert(session);

		Message message = new Message();
		message.setSessionId(DigestUtil.md5Hex(sessionId.getBytes()));
		message.setSenderId(RobotConstant.ROBOT_ID);
		message.setSenderNickName(RobotConstant.ROBOT_NICKNAME);
		message.setReceiverId(id);
		message.setType(0);
		message.setContent(RobotConstant.HELLO_MESSAGE);
		message.setSendTime(LocalDateTime.now());
		message.setContactType(0);
		message.setStatus(1);
		messageService.insert(message);
	}

	@Override
	public String login(UserLoginDTO userLoginDTO) {
		// 校验验证码
		if (userLoginDTO.getCaptchaKey() == null) {
			throw new BusinessException("请输入验证码");
		}
		if(!userLoginDTO.getCaptchaValue().equals(
				redisTemplate.opsForValue().get(userLoginDTO.getCaptchaKey()))) {
			redisTemplate.delete(userLoginDTO.getCaptchaKey());
			throw new BusinessException("验证码不正确");
		}
		redisTemplate.delete(userLoginDTO.getCaptchaKey());

		// 校验用户名以及密码
		User user = userMapper.selectByEmail(userLoginDTO.getEmail());
		// 将传入的密码进行SHA256加密
		String password = DigestUtil.sha256Hex(userLoginDTO.getPassword().getBytes());
		if (user == null || !password.equals(user.getPassword())) {
			throw new BusinessException("错误的用户名或密码");
		}

		// 判断用户是否被封禁
		if (user.getStatus() == 0) {
			throw new BusinessException("账号已被封禁");
		}

		// 判断用户是否已登录


		// 查询用户好友列表、群组列表、会话列表、离线期间收到的消息和好友申请，放在redis中
		List<Contact> contacts = contactService.selectByInitiatorIdOrAccepterId(
				user.getId(), user.getId());
		List<Session> sessions = sessionService.selectByInitiatorIdOrAccepterId(user.getId(), user.getId());
		List<Message> messages = messageService.selectByReceiverId(user.getId());
		System.out.println(contacts);
		System.out.println(sessions);
		System.out.println(messages);

		// 生成JWT
		Map<String, Object> payload = new HashMap<>();
		payload.put("id", user.getId());
		payload.put("email", user.getEmail());
		payload.put("nickname", user.getNickname());
		String jwt = Jwts.builder().setClaims(payload)
				.signWith(SignatureAlgorithm.HS256, UserConstant.SECRET_KEY)
				.setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
				.compact();
		user.setLastLoginTime(LocalDateTime.now());
		return jwt;
	}
}
