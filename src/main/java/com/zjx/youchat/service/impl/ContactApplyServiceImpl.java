package com.zjx.youchat.service.impl;

import com.zjx.youchat.mapper.ContactApplyMapper;
import com.zjx.youchat.pojo.po.ContactApply;
import com.zjx.youchat.service.ContactApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactApplyServiceImpl implements ContactApplyService {
	@Autowired
	private ContactApplyMapper contactApplyMapper;

	@Override
	public void insertContactApply(ContactApply contactApply) {
		contactApplyMapper.insertContactApply(contactApply);
	}

	@Override
	public List<ContactApply> selectContactApply() {
		return contactApplyMapper.selectContactApply();
	}

	@Override
	public void updateContactApplyById(Integer id, ContactApply contactApply) {
		contactApplyMapper.updateContactApplyById(id, contactApply);
	}

	@Override
	public void deleteContactApplyById(Integer id) {
		contactApplyMapper.deleteContactApplyById(id);
	}

	@Override
	public ContactApply selectContactApplyById(Integer id) {
		return contactApplyMapper.selectContactApplyById(id);
	}
}
