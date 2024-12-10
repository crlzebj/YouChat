package com.zjx.youchat.service.impl;

import com.zjx.youchat.mapper.ChatSessionMapper;
import com.zjx.youchat.pojo.po.ChatSession;
import com.zjx.youchat.pojo.vo.PageVO;

import com.zjx.youchat.service.ChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatSessionServiceImpl implements ChatSessionService {
	@Autowired
	private ChatSessionMapper chatSessionMapper;

	@Override
	public void insert(ChatSession chatSession) {
		chatSessionMapper.insert(chatSession);
	}

	@Override
	public List<ChatSession> select() {
		return chatSessionMapper.select(new ChatSession());
	}

	@Override
	public List<ChatSession> select(ChatSession chatSession) {
		return chatSessionMapper.select(chatSession);
	}

	@Override
	public Integer count() {
		return chatSessionMapper.count(new ChatSession());
	}

	@Override
	public Integer count(ChatSession chatSession) {
		return chatSessionMapper.count(chatSession);
	}

	@Override
	public PageVO<ChatSession> selectPage(Integer pageSize, Integer pageNum) {
		PageVO<ChatSession> pageVO = new PageVO<ChatSession>();
		pageVO.setTotalSize(count());
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count() + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(chatSessionMapper.selectPage(new ChatSession(), (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public PageVO<ChatSession> selectPage(ChatSession chatSession, Integer pageSize, Integer pageNum) {
		PageVO<ChatSession> pageVO = new PageVO<ChatSession>();
		pageVO.setTotalSize(count(chatSession));
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count(chatSession) + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(chatSessionMapper.selectPage(chatSession, (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}


	@Override
	public void updateBySessionId(String sessionId, ChatSession chatSession) {
		chatSessionMapper.updateBySessionId(sessionId, chatSession);
	}

	@Override
	public void deleteBySessionId(String sessionId) {
		chatSessionMapper.deleteBySessionId(sessionId);
	}

	@Override
	public ChatSession selectBySessionId(String sessionId) {
		return chatSessionMapper.selectBySessionId(sessionId);
	}
}
