package com.zjx.youchat.mapper;

import com.zjx.youchat.pojo.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
	void insertUser(User user);

	List<User> selectUser();

	void updateUserById(@Param("id") String id, @Param("user") User user);

	void deleteUserById(@Param("id") String id);

	User selectUserById(@Param("id") String id);

	void updateUserByEmail(@Param("email") String email, @Param("user") User user);

	void deleteUserByEmail(@Param("email") String email);

	User selectUserByEmail(@Param("email") String email);
}
