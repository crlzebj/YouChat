package com.zjx.youchat.service;

import com.zjx.youchat.pojo.entity.User;

import java.util.List;

public interface UserService {
	void insertUser(User user);

	List<User> selectUser();

	void updateUserById(String id, User user);

	void deleteUserById(String id);

	User selectUserById(String id);

	void updateUserByEmail(String email, User user);

	void deleteUserByEmail(String email);

	User selectUserByEmail(String email);

	String getCaptcha();
}
