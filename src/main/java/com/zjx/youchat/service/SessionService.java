package com.zjx.youchat.service;

import com.zjx.youchat.pojo.po.Session;
import com.zjx.youchat.pojo.vo.PageVO;

import java.util.List;

public interface SessionService {
	void insert(Session session);

	List<Session> select();
	List<Session> select(Session session);

	Integer count();
	Integer count(Session session);

	PageVO<Session> selectPage(Integer pageSize, Integer pageNum);
	PageVO<Session> selectPage(Session session, Integer pageSize, Integer pageNum);

	void updateByInitiatorIdAndAccepterId(String initiatorId, String accepterId, Session session);

	void deleteByInitiatorIdAndAccepterId(String initiatorId, String accepterId);

	Session selectByInitiatorIdAndAccepterId(String initiatorId, String accepterId);

	void updateById(String id, Session session);

	void deleteById(String id);

	Session selectById(String id);

	List<Session> selectByInitiatorIdOrAccepterId(String initiatorId, String accepterId);
}
