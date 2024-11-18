package com.zjx.youchat.service;

import com.zjx.youchat.pojo.dto.UserLoginDTO;
import com.zjx.youchat.pojo.dto.UserRegisterDTO;
import com.zjx.youchat.pojo.po.User;

import java.util.List;
import java.util.Map;

public interface UserService {
	void insertUser(User user);

	List<User> selectUser();

	void updateUserById(String id, User user);

	void deleteUserById(String id);

	User selectUserById(String id);

	void updateUserByEmail(String email, User user);

	void deleteUserByEmail(String email);

	User selectUserByEmail(String email);

	Map<String, String> getCaptcha();

	void register(UserRegisterDTO userRegisterDTO);

	String login(UserLoginDTO userLoginDTO);
}
