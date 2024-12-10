package com.zjx.youchat.mapper;

import com.zjx.youchat.pojo.po.ChatSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatSessionMapper {
	void insert(ChatSession chatSession);

	List<ChatSession> select(ChatSession chatSession);

	Integer count(ChatSession chatSession);

	List<ChatSession> selectPage(@Param("chatSession") ChatSession chatSession, @Param("offset") Integer offset, @Param("count") Integer count);

	void updateBySessionId(@Param("sessionId") String sessionId, @Param("chatSession") ChatSession chatSession);

	void deleteBySessionId(@Param("sessionId") String sessionId);

	ChatSession selectBySessionId(@Param("sessionId") String sessionId);
}
