package com.zjx.youchat.service.impl;

import com.zjx.youchat.mapper.MessageMapper;
import com.zjx.youchat.pojo.po.Message;
import com.zjx.youchat.pojo.vo.PageVO;
import com.zjx.youchat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	private MessageMapper messageMapper;

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
}
