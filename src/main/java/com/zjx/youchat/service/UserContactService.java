package com.zjx.youchat.service;

import com.zjx.youchat.pojo.po.UserContact;

import java.util.List;

public interface UserContactService {
	void insertUserContact(UserContact userContact);

	List<UserContact> selectUserContact();

	void updateUserContactByUserIdAndContactId(String userId, String contactId, UserContact userContact);

	void deleteUserContactByUserIdAndContactId(String userId, String contactId);

	UserContact selectUserContactByUserIdAndContactId(String userId, String contactId);
}
