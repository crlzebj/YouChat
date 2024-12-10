package com.zjx.youchat.service.impl;

import com.zjx.youchat.mapper.ChatSessionUserMapper;
import com.zjx.youchat.pojo.po.ChatSessionUser;
import com.zjx.youchat.pojo.vo.PageVO;

import com.zjx.youchat.service.ChatSessionUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatSessionUserServiceImpl implements ChatSessionUserService {
	@Autowired
	private ChatSessionUserMapper chatSessionUserMapper;

	@Override
	public void insert(ChatSessionUser chatSessionUser) {
		chatSessionUserMapper.insert(chatSessionUser);
	}

	@Override
	public List<ChatSessionUser> select() {
		return chatSessionUserMapper.select(new ChatSessionUser());
	}

	@Override
	public List<ChatSessionUser> select(ChatSessionUser chatSessionUser) {
		return chatSessionUserMapper.select(chatSessionUser);
	}

	@Override
	public Integer count() {
		return chatSessionUserMapper.count(new ChatSessionUser());
	}

	@Override
	public Integer count(ChatSessionUser chatSessionUser) {
		return chatSessionUserMapper.count(chatSessionUser);
	}

	@Override
	public PageVO<ChatSessionUser> selectPage(Integer pageSize, Integer pageNum) {
		PageVO<ChatSessionUser> pageVO = new PageVO<ChatSessionUser>();
		pageVO.setTotalSize(count());
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count() + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(chatSessionUserMapper.selectPage(new ChatSessionUser(), (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public PageVO<ChatSessionUser> selectPage(ChatSessionUser chatSessionUser, Integer pageSize, Integer pageNum) {
		PageVO<ChatSessionUser> pageVO = new PageVO<ChatSessionUser>();
		pageVO.setTotalSize(count(chatSessionUser));
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count(chatSessionUser) + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(chatSessionUserMapper.selectPage(chatSessionUser, (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}


	@Override
	public void updateByUserIdAndContactId(String userId, String contactId, ChatSessionUser chatSessionUser) {
		chatSessionUserMapper.updateByUserIdAndContactId(userId, contactId, chatSessionUser);
	}

	@Override
	public void deleteByUserIdAndContactId(String userId, String contactId) {
		chatSessionUserMapper.deleteByUserIdAndContactId(userId, contactId);
	}

	@Override
	public ChatSessionUser selectByUserIdAndContactId(String userId, String contactId) {
		return chatSessionUserMapper.selectByUserIdAndContactId(userId, contactId);
	}
}
