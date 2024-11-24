package com.zjx.youchat.service;

import com.zjx.youchat.pojo.dto.UserLoginDTO;
import com.zjx.youchat.pojo.dto.UserRegisterDTO;
import com.zjx.youchat.pojo.po.User;
import com.zjx.youchat.pojo.vo.CaptchaVO;
import com.zjx.youchat.pojo.vo.PageVO;

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
}
