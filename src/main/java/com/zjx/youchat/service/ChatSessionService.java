package com.zjx.youchat.service;

import com.zjx.youchat.pojo.po.ChatSession;
import com.zjx.youchat.pojo.vo.PageVO;

import java.util.List;

public interface ChatSessionService {
	void insert(ChatSession chatSession);

	List<ChatSession> select();
	List<ChatSession> select(ChatSession chatSession);

	Integer count();
	Integer count(ChatSession chatSession);

	PageVO<ChatSession> selectPage(Integer pageSize, Integer pageNum);
	PageVO<ChatSession> selectPage(ChatSession chatSession, Integer pageSize, Integer pageNum);

	void updateBySessionId(String sessionId, ChatSession chatSession);

	void deleteBySessionId(String sessionId);

	ChatSession selectBySessionId(String sessionId);
}
