package com.zjx.youchat.service;

import com.zjx.youchat.pojo.po.ChatMessage;
import com.zjx.youchat.pojo.vo.PageVO;

import java.util.List;

public interface ChatMessageService {
	void insert(ChatMessage chatMessage);

	List<ChatMessage> select();
	List<ChatMessage> select(ChatMessage chatMessage);

	Integer count();
	Integer count(ChatMessage chatMessage);

	PageVO<ChatMessage> selectPage(Integer pageSize, Integer pageNum);
	PageVO<ChatMessage> selectPage(ChatMessage chatMessage, Integer pageSize, Integer pageNum);

	void updateByMessageId(Long messageId, ChatMessage chatMessage);

	void deleteByMessageId(Long messageId);

	ChatMessage selectByMessageId(Long messageId);
}
