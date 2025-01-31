package com.zjx.youchat.chat.service.impl;

import com.zjx.youchat.chat.mapper.SessionMapper;
import com.zjx.youchat.chat.domain.po.Session;
import com.zjx.youchat.chat.domain.vo.PageVO;
import com.zjx.youchat.chat.service.SessionService;
import com.zjx.youchat.chat.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionServiceImpl implements SessionService {
	@Autowired
	private SessionMapper sessionMapper;

	@Override
	public void insert(Session session) {
		sessionMapper.insert(session);
	}

	@Override
	public List<Session> select() {
		return sessionMapper.select(new Session());
	}

	@Override
	public List<Session> select(Session session) {
		return sessionMapper.select(session);
	}

	@Override
	public Integer count() {
		return sessionMapper.count(new Session());
	}

	@Override
	public Integer count(Session session) {
		return sessionMapper.count(session);
	}

	@Override
	public PageVO<Session> selectPage(Integer pageSize, Integer pageNum) {
		PageVO<Session> pageVO = new PageVO<Session>();
		pageVO.setTotalSize(count());
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count() + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(sessionMapper.selectPage(new Session(), (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public PageVO<Session> selectPage(Session session, Integer pageSize, Integer pageNum) {
		PageVO<Session> pageVO = new PageVO<Session>();
		pageVO.setTotalSize(count(session));
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count(session) + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(sessionMapper.selectPage(session, (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}


	@Override
	public void updateByInitiatorIdAndAccepterId(String initiatorId, String accepterId, Session session) {
		sessionMapper.updateByInitiatorIdAndAccepterId(initiatorId, accepterId, session);
	}

	@Override
	public void deleteByInitiatorIdAndAccepterId(String initiatorId, String accepterId) {
		sessionMapper.deleteByInitiatorIdAndAccepterId(initiatorId, accepterId);
	}

	@Override
	public Session selectByInitiatorIdAndAccepterId(String initiatorId, String accepterId) {
		return sessionMapper.selectByInitiatorIdAndAccepterId(initiatorId, accepterId);
	}

	@Override
	public void updateById(String id, Session session) {
		sessionMapper.updateById(id, session);
	}

	@Override
	public void deleteById(String id) {
		sessionMapper.deleteById(id);
	}

	@Override
	public Session selectById(String id) {
		return sessionMapper.selectById(id);
	}

	@Override
	public List<Session> getMySession() {
		String userId = ThreadLocalUtil.getUserId();
		return sessionMapper.selectSession(userId);
	}
}
