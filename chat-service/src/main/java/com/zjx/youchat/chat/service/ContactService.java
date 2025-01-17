package com.zjx.youchat.chat.service;

import com.zjx.youchat.chat.domain.po.Contact;
import com.zjx.youchat.chat.domain.po.Group;
import com.zjx.youchat.chat.domain.vo.PageVO;
import com.zjx.youchat.chat.domain.vo.UserViewVO;

import java.util.List;

public interface ContactService {
	void insert(Contact contact);

	List<Contact> select();
	List<Contact> select(Contact contact);

	Integer count();
	Integer count(Contact contact);

	PageVO<Contact> selectPage(Integer pageSize, Integer pageNum);
	PageVO<Contact> selectPage(Contact contact, Integer pageSize, Integer pageNum);

	void updateByInitiatorIdAndAccepterId(String initiatorId, String accepterId, Contact contact);

	void deleteByInitiatorIdAndAccepterId(String initiatorId, String accepterId);

	Contact selectByInitiatorIdAndAccepterId(String initiatorId, String accepterId);

	UserViewVO viewUser(String userId);

	Group viewGroup(String groupId);
}
