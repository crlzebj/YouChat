package com.zjx.youchat.service.impl;

import com.zjx.youchat.mapper.ContactApplicationMapper;
import com.zjx.youchat.pojo.po.ContactApplication;
import com.zjx.youchat.pojo.vo.PageVO;

import com.zjx.youchat.service.ContactApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactApplicationServiceImpl implements ContactApplicationService {
	@Autowired
	private ContactApplicationMapper contactApplicationMapper;

	@Override
	public void insert(ContactApplication contactApplication) {
		contactApplicationMapper.insert(contactApplication);
	}

	@Override
	public List<ContactApplication> select() {
		return contactApplicationMapper.select(new ContactApplication());
	}

	@Override
	public List<ContactApplication> select(ContactApplication contactApplication) {
		return contactApplicationMapper.select(contactApplication);
	}

	@Override
	public Integer count() {
		return contactApplicationMapper.count(new ContactApplication());
	}

	@Override
	public Integer count(ContactApplication contactApplication) {
		return contactApplicationMapper.count(contactApplication);
	}

	@Override
	public PageVO<ContactApplication> selectPage(Integer pageSize, Integer pageNum) {
		PageVO<ContactApplication> pageVO = new PageVO<ContactApplication>();
		pageVO.setTotalSize(count());
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count() + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(contactApplicationMapper.selectPage(new ContactApplication(), (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public PageVO<ContactApplication> selectPage(ContactApplication contactApplication, Integer pageSize, Integer pageNum) {
		PageVO<ContactApplication> pageVO = new PageVO<ContactApplication>();
		pageVO.setTotalSize(count(contactApplication));
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count(contactApplication) + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(contactApplicationMapper.selectPage(contactApplication, (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}


	@Override
	public void updateById(Integer id, ContactApplication contactApplication) {
		contactApplicationMapper.updateById(id, contactApplication);
	}

	@Override
	public void deleteById(Integer id) {
		contactApplicationMapper.deleteById(id);
	}

	@Override
	public ContactApplication selectById(Integer id) {
		return contactApplicationMapper.selectById(id);
	}
}
