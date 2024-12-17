package com.zjx.youchat.service.impl;

import com.zjx.youchat.configuration.ProjectConfig;
import com.zjx.youchat.exception.BusinessException;
import com.zjx.youchat.mapper.ChatGroupMapper;
import com.zjx.youchat.mapper.UserContactMapper;
import com.zjx.youchat.pojo.dto.ChatGroupRegisterDTO;
import com.zjx.youchat.pojo.po.ChatGroup;
import com.zjx.youchat.pojo.po.UserContact;
import com.zjx.youchat.pojo.vo.PageVO;

import com.zjx.youchat.service.ChatGroupService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatGroupServiceImpl implements ChatGroupService {
	@Autowired
	private ProjectConfig projectConfig;

	@Autowired
	private ChatGroupMapper chatGroupMapper;

	@Autowired
	private UserContactMapper userContactMapper;

	@Override
	public void insert(ChatGroup chatGroup) {
		chatGroupMapper.insert(chatGroup);
	}

	@Override
	public List<ChatGroup> select() {
		return chatGroupMapper.select(new ChatGroup());
	}

	@Override
	public List<ChatGroup> select(ChatGroup chatGroup) {
		return chatGroupMapper.select(chatGroup);
	}

	@Override
	public Integer count() {
		return chatGroupMapper.count(new ChatGroup());
	}

	@Override
	public Integer count(ChatGroup chatGroup) {
		return chatGroupMapper.count(chatGroup);
	}

	@Override
	public PageVO<ChatGroup> selectPage(Integer pageSize, Integer pageNum) {
		PageVO<ChatGroup> pageVO = new PageVO<ChatGroup>();
		pageVO.setTotalSize(count());
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count() + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(chatGroupMapper.selectPage(new ChatGroup(), (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public PageVO<ChatGroup> selectPage(ChatGroup chatGroup, Integer pageSize, Integer pageNum) {
		PageVO<ChatGroup> pageVO = new PageVO<ChatGroup>();
		pageVO.setTotalSize(count(chatGroup));
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count(chatGroup) + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(chatGroupMapper.selectPage(chatGroup, (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}


	@Override
	public void updateById(String id, ChatGroup chatGroup) {
		chatGroupMapper.updateById(id, chatGroup);
	}

	@Override
	public void deleteById(String id) {
		chatGroupMapper.deleteById(id);
	}

	@Override
	public ChatGroup selectById(String id) {
		return chatGroupMapper.selectById(id);
	}

	@Override
	@Transactional
	public void register(String ownerId, ChatGroupRegisterDTO chatGroupRegisterDTO) {
		ChatGroup chatGroup = new ChatGroup();
		// 为新用户创建id
		String id = "G" + RandomStringUtils.random(7, false, true);
		while (selectById(id) != null) {
			id = "G" + RandomStringUtils.random(7, false, true);
		}
		chatGroup.setId(id);
		chatGroup.setStatus(1);
		chatGroup.setPermission(chatGroupRegisterDTO.getPermission());
		chatGroup.setNickname(chatGroupRegisterDTO.getName());
		chatGroup.setOwnerId(ownerId);
		chatGroup.setGroupNotice(chatGroupRegisterDTO.getGroupNotice());
		chatGroup.setCreateTime(LocalDateTime.now());
		try {
			Path youchatDataPath = Paths.get(projectConfig.getServerDataPath(), "avatar");
			if (!Files.exists(youchatDataPath)) {
				Files.createDirectories(youchatDataPath);
			}
			File file1 = new File(youchatDataPath.toString(), chatGroup.getId() + ".png");
			chatGroupRegisterDTO.getAvatarFile().transferTo(file1.getAbsoluteFile());
			File file2 = new File(youchatDataPath.toString(), chatGroup.getId() + "_cover.png");
			chatGroupRegisterDTO.getAvatarThumbFile().transferTo(file2.getAbsoluteFile());
		} catch (Exception e) {
			throw new BusinessException("头像上传失败，请稍后重试");
		}
		chatGroupMapper.insert(chatGroup);

		UserContact userContact = new UserContact();
		userContact.setUserId(ownerId);
		userContact.setContactId(chatGroup.getId());
		userContact.setType(1);
		userContact.setContactPermission(1);
		userContact.setCreateTime(LocalDateTime.now());
		userContact.setLastUpdateTime(LocalDateTime.now());
		userContactMapper.insert(userContact);
	}
}
