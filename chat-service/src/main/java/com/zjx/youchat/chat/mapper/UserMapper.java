package com.zjx.youchat.chat.mapper;

import com.zjx.youchat.chat.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
	void insert(User user);

	List<User> select(User user);

	Integer count(User user);

	List<User> selectPage(@Param("user") User user, @Param("offset") Integer offset, @Param("count") Integer count);

	void updateById(@Param("id") String id, @Param("user") User user);

	void deleteById(@Param("id") String id);

	User selectById(@Param("id") String id);

	void updateByEmail(@Param("email") String email, @Param("user") User user);

	void deleteByEmail(@Param("email") String email);

	User selectByEmail(@Param("email") String email);
}
