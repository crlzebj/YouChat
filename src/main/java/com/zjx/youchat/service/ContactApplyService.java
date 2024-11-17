package com.zjx.youchat.service;

import com.zjx.youchat.pojo.po.ContactApply;

import java.util.List;

public interface ContactApplyService {
	void insertContactApply(ContactApply contactApply);

	List<ContactApply> selectContactApply();

	void updateContactApplyById(Integer id, ContactApply contactApply);

	void deleteContactApplyById(Integer id);

	ContactApply selectContactApplyById(Integer id);
}
