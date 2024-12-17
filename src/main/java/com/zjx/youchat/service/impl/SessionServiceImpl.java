package com.zjx.youchat.service.impl;

import com.zjx.youchat.mapper.SessionMapper;
import com.zjx.youchat.pojo.po.Session;
import com.zjx.youchat.pojo.vo.PageVO;

import com.zjx.youchat.service.SessionService;
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
	public void updateByUserIdAndContactId(String userId, String contactId, Session session) {
		sessionMapper.updateByUserIdAndContactId(userId, contactId, session);
	}

	@Override
	public void deleteByUserIdAndContactId(String userId, String contactId) {
		sessionMapper.deleteByUserIdAndContactId(userId, contactId);
	}

	@Override
	public Session selectByUserIdAndContactId(String userId, String contactId) {
		return sessionMapper.selectByUserIdAndContactId(userId, contactId);
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
}
