package com.zjx.youchat.service.impl;

import com.zjx.youchat.constant.UserConstant;
import com.zjx.youchat.mapper.ChatGroupMapper;
import com.zjx.youchat.pojo.dto.ChatGroupRegisterDTO;
import com.zjx.youchat.pojo.po.ChatGroup;
import com.zjx.youchat.service.ChatGroupService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

	@Override
	public void register(String ownerId, ChatGroupRegisterDTO chatGroupRegisterDTO) {
		ChatGroup chatGroup = new ChatGroup();
		chatGroup.setId(chatGroupRegisterDTO.getId());
		chatGroup.setStatus(1);
		chatGroup.setPermission(chatGroupRegisterDTO.getPermission());
		chatGroup.setName(chatGroupRegisterDTO.getName());
		chatGroup.setOwnerId(ownerId);
		chatGroup.setGroupNotice(chatGroupRegisterDTO.getGroupNotice());
		chatGroup.setCreateTime(LocalDateTime.now());
		chatGroupMapper.insertChatGroup(chatGroup);
	}
}
