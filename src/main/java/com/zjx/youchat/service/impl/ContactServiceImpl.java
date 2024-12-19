package com.zjx.youchat.service.impl;

import com.zjx.youchat.mapper.ContactMapper;
import com.zjx.youchat.pojo.po.Contact;
import com.zjx.youchat.pojo.vo.PageVO;

import com.zjx.youchat.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
	@Autowired
	private ContactMapper contactMapper;

	@Override
	public void insert(Contact contact) {
		contactMapper.insert(contact);
	}

	@Override
	public List<Contact> select() {
		return contactMapper.select(new Contact());
	}

	@Override
	public List<Contact> select(Contact contact) {
		return contactMapper.select(contact);
	}

	@Override
	public Integer count() {
		return contactMapper.count(new Contact());
	}

	@Override
	public Integer count(Contact contact) {
		return contactMapper.count(contact);
	}

	@Override
	public PageVO<Contact> selectPage(Integer pageSize, Integer pageNum) {
		PageVO<Contact> pageVO = new PageVO<Contact>();
		pageVO.setTotalSize(count());
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count() + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(contactMapper.selectPage(new Contact(), (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public PageVO<Contact> selectPage(Contact contact, Integer pageSize, Integer pageNum) {
		PageVO<Contact> pageVO = new PageVO<Contact>();
		pageVO.setTotalSize(count(contact));
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count(contact) + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(contactMapper.selectPage(contact, (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}


	@Override
	public void updateByInitiatorIdAndAccepterId(String initiatorId, String accepterId, Contact contact) {
		contactMapper.updateByInitiatorIdAndAccepterId(initiatorId, accepterId, contact);
	}

	@Override
	public void deleteByInitiatorIdAndAccepterId(String initiatorId, String accepterId) {
		contactMapper.deleteByInitiatorIdAndAccepterId(initiatorId, accepterId);
	}

	@Override
	public Contact selectByInitiatorIdAndAccepterId(String initiatorId, String accepterId) {
		return contactMapper.selectByInitiatorIdAndAccepterId(initiatorId, accepterId);
	}

	@Override
	public List<Contact> selectByInitiatorIdOrAccepterId(String initiatorId, String accepterId) {
		return contactMapper.selectByInitiatorIdOrAccepterId(initiatorId, accepterId);
	}
}
