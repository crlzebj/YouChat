package com.zjx.youchat.service;

import com.zjx.youchat.pojo.po.UserContact;
import com.zjx.youchat.pojo.vo.PageVO;

import java.util.List;

public interface UserContactService {
	void insert(UserContact userContact);

	List<UserContact> select();
	List<UserContact> select(UserContact userContact);

	Integer count();
	Integer count(UserContact userContact);

	PageVO<UserContact> selectPage(Integer pageSize, Integer pageNum);
	PageVO<UserContact> selectPage(UserContact userContact, Integer pageSize, Integer pageNum);

	void updateByUserIdAndContactId(String userId, String contactId, UserContact userContact);

	void deleteByUserIdAndContactId(String userId, String contactId);

	UserContact selectByUserIdAndContactId(String userId, String contactId);

	List<UserContact> selectByUserIdOrContactId(String userId, String contactId);
}
