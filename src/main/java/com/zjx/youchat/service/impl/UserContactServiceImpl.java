package com.zjx.youchat.service.impl;

import com.zjx.youchat.mapper.UserContactMapper;
import com.zjx.youchat.pojo.po.UserContact;
import com.zjx.youchat.service.UserContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserContactServiceImpl implements UserContactService {
	@Autowired
	private UserContactMapper userContactMapper;

	@Override
	public void insertUserContact(UserContact userContact) {
		userContactMapper.insertUserContact(userContact);
	}

	@Override
	public List<UserContact> selectUserContact() {
		return userContactMapper.selectUserContact();
	}

	@Override
	public void updateUserContactByUserIdAndContactId(String userId, String contactId, UserContact userContact) {
		userContactMapper.updateUserContactByUserIdAndContactId(userId, contactId, userContact);
	}

	@Override
	public void deleteUserContactByUserIdAndContactId(String userId, String contactId) {
		userContactMapper.deleteUserContactByUserIdAndContactId(userId, contactId);
	}

	@Override
	public UserContact selectUserContactByUserIdAndContactId(String userId, String contactId) {
		return userContactMapper.selectUserContactByUserIdAndContactId(userId, contactId);
	}
}
