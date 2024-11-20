package com.zjx.youchat.service.impl;

import com.zjx.youchat.configuration.ProjectConfig;
import com.zjx.youchat.exception.BusinessException;
import com.zjx.youchat.mapper.ChatGroupMapper;
import com.zjx.youchat.mapper.UserContactMapper;
import com.zjx.youchat.pojo.dto.ChatGroupRegisterDTO;
import com.zjx.youchat.pojo.po.ChatGroup;
import com.zjx.youchat.pojo.po.UserContact;
import com.zjx.youchat.service.ChatGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatGroupServiceImpl implements ChatGroupService {
	@Autowired
	private ChatGroupMapper chatGroupMapper;

	@Autowired
	private UserContactMapper userContactMapper;

	@Autowired
	private ProjectConfig projectConfig;

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
	@Transactional
	public void register(String ownerId, ChatGroupRegisterDTO chatGroupRegisterDTO) {
		ChatGroup chatGroup = new ChatGroup();
		chatGroup.setId(chatGroupRegisterDTO.getId());
		chatGroup.setStatus(1);
		chatGroup.setPermission(chatGroupRegisterDTO.getPermission());
		chatGroup.setName(chatGroupRegisterDTO.getName());
		chatGroup.setOwnerId(ownerId);
		chatGroup.setGroupNotice(chatGroupRegisterDTO.getGroupNotice());
		chatGroup.setCreateTime(LocalDateTime.now());
		try {
			File file1 = new File(projectConfig.getServerDataPath(), "1.png");
			chatGroupRegisterDTO.getAvatarFile().transferTo(file1.getAbsoluteFile());
			File file2 = new File(projectConfig.getServerDataPath(), "2.png");
			chatGroupRegisterDTO.getAvatarThumbFile().transferTo(file2.getAbsoluteFile());
		} catch (Exception e) {
			throw new BusinessException("头像上传失败，请稍后重试");
		}
		chatGroupMapper.insertChatGroup(chatGroup);

		UserContact userContact = new UserContact();
		userContact.setUserId(ownerId);
		userContact.setContactId(chatGroup.getId());
		userContact.setContactType(1);
		userContact.setContactPermission(1);
		userContact.setCreateTime(LocalDateTime.now());
		userContact.setLastUpdateTime(LocalDateTime.now());
		userContactMapper.insertUserContact(userContact);
	}
}
