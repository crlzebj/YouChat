package com.zjx.youchat.chat.service;

import com.zjx.youchat.chat.domain.dto.MessageSendDTO;
import com.zjx.youchat.chat.domain.po.Message;
import com.zjx.youchat.chat.domain.vo.PageVO;

import java.util.List;

public interface MessageService {
	void insert(Message message);

	List<Message> select();
	List<Message> select(Message message);

	Integer count();
	Integer count(Message message);

	PageVO<Message> selectPage(Integer pageSize, Integer pageNum);
	PageVO<Message> selectPage(Message message, Integer pageSize, Integer pageNum);

	void updateById(Long id, Message message);

	void deleteById(Long id);

	Message selectById(Long id);

	void send(MessageSendDTO messageSendDTO);
}
