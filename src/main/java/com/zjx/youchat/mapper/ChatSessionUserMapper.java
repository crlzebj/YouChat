package com.zjx.youchat.mapper;

import com.zjx.youchat.pojo.po.ChatSessionUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatSessionUserMapper {
	void insert(ChatSessionUser chatSessionUser);

	List<ChatSessionUser> select(ChatSessionUser chatSessionUser);

	Integer count(ChatSessionUser chatSessionUser);

	List<ChatSessionUser> selectPage(@Param("chatSessionUser") ChatSessionUser chatSessionUser, @Param("offset") Integer offset, @Param("count") Integer count);

	void updateByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId, @Param("chatSessionUser") ChatSessionUser chatSessionUser);

	void deleteByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId);

	ChatSessionUser selectByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId);
}
