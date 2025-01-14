package com.zjx.youchat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.wf.captcha.ArithmeticCaptcha;
import com.zjx.youchat.constant.ExceptionConstant;
import com.zjx.youchat.constant.RobotConstant;
import com.zjx.youchat.constant.UserConstant;
import com.zjx.youchat.exception.BusinessException;
import com.zjx.youchat.mapper.ContactMapper;
import com.zjx.youchat.mapper.MessageMapper;
import com.zjx.youchat.mapper.SessionMapper;
import com.zjx.youchat.mapper.UserMapper;
import com.zjx.youchat.pojo.dto.UserInfoDTO;
import com.zjx.youchat.pojo.dto.UserLoginDTO;
import com.zjx.youchat.pojo.dto.UserRegisterDTO;
import com.zjx.youchat.pojo.po.Contact;
import com.zjx.youchat.pojo.po.Message;
import com.zjx.youchat.pojo.po.Session;
import com.zjx.youchat.pojo.po.User;
import com.zjx.youchat.pojo.vo.CaptchaVO;
import com.zjx.youchat.pojo.vo.PageVO;
import com.zjx.youchat.pojo.vo.UserQueryVO;
import com.zjx.youchat.pojo.vo.UserViewVO;
import com.zjx.youchat.service.UserService;
import com.zjx.youchat.util.RedisUtil;
import com.zjx.youchat.util.ThreadLocalUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ContactMapper contactMapper;

	@Autowired
	private SessionMapper sessionMapper;

	@Autowired
	private MessageMapper messageMapper;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private RedisUtil redisUtil;

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
		String id = UUID.randomUUID().toString();
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);

		// 在redis中存入随机的验证码key以及验证码的正确结果
		redisTemplate.opsForValue().set(UserConstant.CAPTCHA_KEY_PREFIX + id,
				captcha.text(), Duration.ofMinutes(1));

		// 将随机的验证码key以及验证码图片的base64编码返回
		CaptchaVO captchaVO = new CaptchaVO();
		captchaVO.setCaptchaKey(UserConstant.CAPTCHA_KEY_PREFIX + id);
		captchaVO.setCaptchaValue(captcha.toBase64());
		return captchaVO;
	}

	@Override
	@Transactional
	public void register(UserRegisterDTO userRegisterDTO) {
		// 校验验证码
		if (userRegisterDTO.getCaptchaKey() == null || userRegisterDTO.getCaptchaValue() == null) {
			throw new BusinessException(ExceptionConstant.CAPTCHA_FAILED);
		}
		if(!userRegisterDTO.getCaptchaValue()
				.equals(redisTemplate.opsForValue().get(userRegisterDTO.getCaptchaKey()))) {
			redisTemplate.delete(userRegisterDTO.getCaptchaKey());
			throw new BusinessException(ExceptionConstant.CAPTCHA_FAILED);
		}
		redisTemplate.delete(userRegisterDTO.getCaptchaKey());

		// 校验邮箱是否可用
		if (selectByEmail(userRegisterDTO.getEmail()) != null) {
			throw new BusinessException(ExceptionConstant.EMAIL_ALREADY_USED);
		}

		// 为新用户分配id
		String userId = RandomStringUtils.random(8, false, true);
		while (selectById(userId) != null) {
			userId = RandomStringUtils.random(8, false, true);
		}

		// 数据库中插入新用户记录
		User user = new User();
		user.setId(userId);
		user.setEmail(userRegisterDTO.getEmail());
		// 对用户密码进行SHA256加密
		String password = DigestUtil.sha256Hex(userRegisterDTO.getPassword().getBytes());
		user.setPassword(password);
		user.setNickname(userRegisterDTO.getNickname());
		user.setStatus(1);
		user.setAuthority(1);
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
		contact.setAccepterId(userId);
		contact.setStatus(0);
		contact.setContactType(0);
		contact.setCreateTime(LocalDateTime.now());
		contact.setLastUpdateTime(LocalDateTime.now());
		contactMapper.insert(contact);

		Session session = new Session();
		String sessionId = RobotConstant.ROBOT_ID + userId;
		session.setId(DigestUtil.md5Hex(sessionId.getBytes()));
		session.setInitiatorId(RobotConstant.ROBOT_ID);
		session.setAccepterId(userId);
		session.setLastMessage(RobotConstant.HELLO_MESSAGE);
		session.setLastSendTime(LocalDateTime.now());
		sessionMapper.insert(session);

		Message message = new Message();
		Long messageId = redisUtil.generateId(UserConstant.MESSAGE_ID_PREFIX);
		message.setId(messageId);
		message.setSessionId(session.getId());
		message.setSenderId(RobotConstant.ROBOT_ID);
		message.setReceiverId(userId);
		message.setSendTime(LocalDateTime.now());
		message.setContent(RobotConstant.HELLO_MESSAGE);
		message.setReceiverType(0);
		message.setType(0);
		message.setStatus(1);
		messageMapper.insert(message);
	}

	@Override
	public String login(UserLoginDTO userLoginDTO) {
		// 校验验证码
		if (userLoginDTO.getCaptchaKey() == null || userLoginDTO.getCaptchaValue() == null) {
			throw new BusinessException(ExceptionConstant.CAPTCHA_FAILED);
		}
		if(!userLoginDTO.getCaptchaValue()
				.equals(redisTemplate.opsForValue().get(userLoginDTO.getCaptchaKey()))) {
			redisTemplate.delete(userLoginDTO.getCaptchaKey());
			throw new BusinessException(ExceptionConstant.CAPTCHA_FAILED);
		}
		redisTemplate.delete(userLoginDTO.getCaptchaKey());

		// 校验用户名以及密码
		User user = userMapper.selectByEmail(userLoginDTO.getEmail());
		// 将传入的密码进行SHA256加密
		String password = DigestUtil.sha256Hex(userLoginDTO.getPassword().getBytes());
		if (user == null || !password.equals(user.getPassword())) {
			throw new BusinessException(ExceptionConstant.ID_OR_PASSWORD_INCORRECT);
		}

		// 判断用户是否被封禁
		if (user.getStatus() == 0) {
			throw new BusinessException(ExceptionConstant.USER_BANNED);
		}

		// TODO判断用户是否已登录
		user.setLastLoginTime(LocalDateTime.now());

		// 生成token
		Long timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
		String token = timestamp + UUID.randomUUID().toString();

		// 将token以及用户信息保存到redis中
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		userInfoDTO.setId(user.getId());
		userInfoDTO.setEmail(user.getEmail());
		// TODO修改token过期时间
		redisTemplate.opsForValue().set(UserConstant.USER_TOKEN.formatted(user.getId()), token);
		redisTemplate.opsForValue().set(UserConstant.TOKEN_PREFIX + token, JSON.toJSONString(userInfoDTO));

		// 给用户返回token
		return token;
	}

	@Override
	public void logout() {
		String token = redisTemplate.opsForValue().get(UserConstant.TOKEN_PREFIX + ThreadLocalUtil.getUserId());
		redisTemplate.delete(UserConstant.TOKEN_PREFIX + ThreadLocalUtil.getUserId());
		if (token == null) {
			return;
		}
		redisTemplate.delete(token);
	}
}
