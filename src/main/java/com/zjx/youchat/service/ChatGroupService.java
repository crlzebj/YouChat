package com.zjx.youchat.service;

import com.zjx.youchat.pojo.dto.ChatGroupRegisterDTO;
import com.zjx.youchat.pojo.po.ChatGroup;

import java.util.List;

public interface ChatGroupService {
	void insertChatGroup(ChatGroup chatGroup);

	List<ChatGroup> selectChatGroup();

	void updateChatGroupById(String id, ChatGroup chatGroup);

	void deleteChatGroupById(String id);

	ChatGroup selectChatGroupById(String id);

	void register(String ownerId, ChatGroupRegisterDTO chatGroupRegisterDTO);
}
