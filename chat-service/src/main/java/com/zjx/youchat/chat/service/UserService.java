package com.zjx.youchat.chat.service;

import com.zjx.youchat.chat.domain.dto.UserLoginDTO;
import com.zjx.youchat.chat.domain.dto.UserRegisterDTO;
import com.zjx.youchat.chat.domain.po.User;
import com.zjx.youchat.chat.domain.vo.CaptchaVO;
import com.zjx.youchat.chat.domain.vo.PageVO;
import com.zjx.youchat.chat.domain.dto.PersonalInfoDTO;

import java.util.List;

public interface UserService {
	void insert(User user);

	List<User> select();
	List<User> select(User user);

	Integer count();
	Integer count(User user);

	PageVO<User> selectPage(Integer pageSize, Integer pageNum);
	PageVO<User> selectPage(User user, Integer pageSize, Integer pageNum);

	void updateById(String id, User user);

	void deleteById(String id);

	User selectById(String id);

	void updateByEmail(String email, User user);

	void deleteByEmail(String email);

	User selectByEmail(String email);

	CaptchaVO getCaptcha();

	void register(UserRegisterDTO userRegisterDTO);

	String login(UserLoginDTO userLoginDTO);

	void logout();

	PersonalInfoDTO getPersonalInfo();
}
