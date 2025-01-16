package com.zjx.youchat.chat.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.zjx.youchat.chat.constant.UserConstant;
import com.zjx.youchat.chat.mapper.MessageMapper;
import com.zjx.youchat.chat.domain.po.Message;
import com.zjx.youchat.chat.domain.vo.PageVO;
import com.zjx.youchat.chat.service.MessageService;
import com.zjx.youchat.chat.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	private MessageMapper messageMapper;

	@Autowired
	private RedisUtil redisUtil;

	@Override
	public void insert(Message message) {
		messageMapper.insert(message);
	}

	@Override
	public List<Message> select() {
		return messageMapper.select(new Message());
	}

	@Override
	public List<Message> select(Message message) {
		return messageMapper.select(message);
	}

	@Override
	public Integer count() {
		return messageMapper.count(new Message());
	}

	@Override
	public Integer count(Message message) {
		return messageMapper.count(message);
	}

	@Override
	public PageVO<Message> selectPage(Integer pageSize, Integer pageNum) {
		PageVO<Message> pageVO = new PageVO<Message>();
		pageVO.setTotalSize(count());
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count() + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(messageMapper.selectPage(new Message(), (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public PageVO<Message> selectPage(Message message, Integer pageSize, Integer pageNum) {
		PageVO<Message> pageVO = new PageVO<Message>();
		pageVO.setTotalSize(count(message));
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count(message) + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(messageMapper.selectPage(message, (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}


	@Override
	public void updateById(Long id, Message message) {
		messageMapper.updateById(id, message);
	}

	@Override
	public void deleteById(Long id) {
		messageMapper.deleteById(id);
	}

	@Override
	public Message selectById(Long id) {
		return messageMapper.selectById(id);
	}

	@Override
	public void send(Message message) {
		Long messageId = redisUtil.generateId(UserConstant.MESSAGE_ID_PREFIX);
		message.setId(messageId);
		String sessionId = null;
		if (message.getReceiverType() == 0) {
			sessionId = message.getSenderId().compareTo(message.getReceiverId()) > 0 ?
					DigestUtil.md5Hex((message.getSenderId() + message.getReceiverId()).getBytes()) :
					DigestUtil.md5Hex((message.getReceiverId() + message.getSenderId()).getBytes());
		} else if (message.getReceiverType() == 1) {
			sessionId = DigestUtil.md5Hex(message.getReceiverId().getBytes());
		}
		message.setSessionId(sessionId);
		message.setSendTime(LocalDateTime.now());
		messageMapper.insert(message);
	}
}
