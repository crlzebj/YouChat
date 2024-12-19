package com.zjx.youchat.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.zjx.youchat.configuration.ProjectConfig;
import com.zjx.youchat.exception.BusinessException;
import com.zjx.youchat.mapper.ChatGroupMapper;
import com.zjx.youchat.pojo.dto.ChatGroupRegisterDTO;
import com.zjx.youchat.pojo.po.ChatGroup;
import com.zjx.youchat.pojo.po.Contact;
import com.zjx.youchat.pojo.po.Session;
import com.zjx.youchat.pojo.vo.PageVO;

import com.zjx.youchat.service.ChatGroupService;
import com.zjx.youchat.service.ContactService;
import com.zjx.youchat.service.SessionService;
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
	private ContactService contactService;

	@Autowired
	private SessionService sessionService;

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
	public void register(String ownerId, String ownerNickname, ChatGroupRegisterDTO chatGroupRegisterDTO) {
		// 数据库中插入新群组记录
		ChatGroup chatGroup = new ChatGroup();
		// 为新群组创建id
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

		/*
			将新群组加入群主好友列表
			为新群组和群主创建会话窗口
		 */
		Contact contact = new Contact();
		contact.setInitiatorId(ownerId);
		contact.setInitiatorNickname(ownerNickname);
		contact.setAccepterId(chatGroup.getId());
		contact.setAccepterNickname(chatGroup.getNickname());
		contact.setType(1);
		contact.setStatus(0);
		contact.setCreateTime(LocalDateTime.now());
		contact.setLastUpdateTime(LocalDateTime.now());
		contactService.insert(contact);

		Session session = new Session();
		session.setInitiatorId(ownerId);
		session.setInitiatorNickname(ownerNickname);
		session.setAccepterId(chatGroup.getId());
		session.setAccepterNickname(chatGroup.getNickname());
		String sessionId = chatGroup.getId() + ownerId;
		session.setId(DigestUtil.md5Hex(sessionId.getBytes()));
		session.setLastMessage(null);
		session.setLastReceiveTime(null);
		sessionService.insert(session);
	}
}
