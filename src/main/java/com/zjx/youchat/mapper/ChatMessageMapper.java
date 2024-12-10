package com.zjx.youchat.mapper;

import com.zjx.youchat.pojo.po.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMessageMapper {
	void insert(ChatMessage chatMessage);

	List<ChatMessage> select(ChatMessage chatMessage);

	Integer count(ChatMessage chatMessage);

	List<ChatMessage> selectPage(@Param("chatMessage") ChatMessage chatMessage, @Param("offset") Integer offset, @Param("count") Integer count);

	void updateByMessageId(@Param("messageId") Long messageId, @Param("chatMessage") ChatMessage chatMessage);

	void deleteByMessageId(@Param("messageId") Long messageId);

	ChatMessage selectByMessageId(@Param("messageId") Long messageId);
}
