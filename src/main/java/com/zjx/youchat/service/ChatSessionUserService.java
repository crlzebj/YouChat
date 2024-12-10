package com.zjx.youchat.service;

import com.zjx.youchat.pojo.po.ChatSessionUser;
import com.zjx.youchat.pojo.vo.PageVO;

import java.util.List;

public interface ChatSessionUserService {
	void insert(ChatSessionUser chatSessionUser);

	List<ChatSessionUser> select();
	List<ChatSessionUser> select(ChatSessionUser chatSessionUser);

	Integer count();
	Integer count(ChatSessionUser chatSessionUser);

	PageVO<ChatSessionUser> selectPage(Integer pageSize, Integer pageNum);
	PageVO<ChatSessionUser> selectPage(ChatSessionUser chatSessionUser, Integer pageSize, Integer pageNum);

	void updateByUserIdAndContactId(String userId, String contactId, ChatSessionUser chatSessionUser);

	void deleteByUserIdAndContactId(String userId, String contactId);

	ChatSessionUser selectByUserIdAndContactId(String userId, String contactId);
}
