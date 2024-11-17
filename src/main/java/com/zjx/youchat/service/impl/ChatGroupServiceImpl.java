package com.zjx.youchat.service.impl;

import com.zjx.youchat.mapper.ChatGroupMapper;
import com.zjx.youchat.pojo.po.ChatGroup;
import com.zjx.youchat.service.ChatGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatGroupServiceImpl implements ChatGroupService {
	@Autowired
	private ChatGroupMapper chatGroupMapper;

	@Override
	public void insertChatGroup(ChatGroup chatGroup) {
		chatGroupMapper.insertChatGroup(chatGroup);
	}

	@Override
	public List<ChatGroup> selectChatGroup() {
		return chatGroupMapper.selectChatGroup();
	}

	@Override
	public void updateChatGroupById(String id, ChatGroup chatGroup) {
		chatGroupMapper.updateChatGroupById(id, chatGroup);
	}

	@Override
	public void deleteChatGroupById(String id) {
		chatGroupMapper.deleteChatGroupById(id);
	}

	@Override
	public ChatGroup selectChatGroupById(String id) {
		return chatGroupMapper.selectChatGroupById(id);
	}
}
