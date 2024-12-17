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

	void updateByUserIdAndContactId(String userId, String contactId, Session session);

	void deleteByUserIdAndContactId(String userId, String contactId);

	Session selectByUserIdAndContactId(String userId, String contactId);

	void updateById(String id, Session session);

	void deleteById(String id);

	Session selectById(String id);
}
