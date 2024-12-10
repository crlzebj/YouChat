package com.zjx.youchat.service.impl;

import com.zjx.youchat.mapper.ChatMessageMapper;
import com.zjx.youchat.pojo.po.ChatMessage;
import com.zjx.youchat.pojo.vo.PageVO;

import com.zjx.youchat.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
	@Autowired
	private ChatMessageMapper chatMessageMapper;

	@Override
	public void insert(ChatMessage chatMessage) {
		chatMessageMapper.insert(chatMessage);
	}

	@Override
	public List<ChatMessage> select() {
		return chatMessageMapper.select(new ChatMessage());
	}

	@Override
	public List<ChatMessage> select(ChatMessage chatMessage) {
		return chatMessageMapper.select(chatMessage);
	}

	@Override
	public Integer count() {
		return chatMessageMapper.count(new ChatMessage());
	}

	@Override
	public Integer count(ChatMessage chatMessage) {
		return chatMessageMapper.count(chatMessage);
	}

	@Override
	public PageVO<ChatMessage> selectPage(Integer pageSize, Integer pageNum) {
		PageVO<ChatMessage> pageVO = new PageVO<ChatMessage>();
		pageVO.setTotalSize(count());
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count() + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(chatMessageMapper.selectPage(new ChatMessage(), (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public PageVO<ChatMessage> selectPage(ChatMessage chatMessage, Integer pageSize, Integer pageNum) {
		PageVO<ChatMessage> pageVO = new PageVO<ChatMessage>();
		pageVO.setTotalSize(count(chatMessage));
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count(chatMessage) + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(chatMessageMapper.selectPage(chatMessage, (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}


	@Override
	public void updateByMessageId(Long messageId, ChatMessage chatMessage) {
		chatMessageMapper.updateByMessageId(messageId, chatMessage);
	}

	@Override
	public void deleteByMessageId(Long messageId) {
		chatMessageMapper.deleteByMessageId(messageId);
	}

	@Override
	public ChatMessage selectByMessageId(Long messageId) {
		return chatMessageMapper.selectByMessageId(messageId);
	}
}
