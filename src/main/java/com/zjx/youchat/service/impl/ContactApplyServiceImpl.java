package com.zjx.youchat.service.impl;

import com.zjx.youchat.mapper.ContactApplyMapper;
import com.zjx.youchat.pojo.po.ContactApply;
import com.zjx.youchat.pojo.vo.PageVO;

import com.zjx.youchat.service.ContactApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactApplyServiceImpl implements ContactApplyService {
	@Autowired
	private ContactApplyMapper contactApplyMapper;

	@Override
	public void insert(ContactApply contactApply) {
		contactApplyMapper.insert(contactApply);
	}

	@Override
	public List<ContactApply> select() {
		return contactApplyMapper.select(new ContactApply());
	}

	@Override
	public List<ContactApply> select(ContactApply contactApply) {
		return contactApplyMapper.select(contactApply);
	}

	@Override
	public Integer count() {
		return contactApplyMapper.count(new ContactApply());
	}

	@Override
	public Integer count(ContactApply contactApply) {
		return contactApplyMapper.count(contactApply);
	}

	@Override
	public PageVO<ContactApply> selectPage(Integer pageSize, Integer pageNum) {
		PageVO<ContactApply> pageVO = new PageVO<ContactApply>();
		pageVO.setTotalSize(count());
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count() + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(contactApplyMapper.selectPage(new ContactApply(), (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public PageVO<ContactApply> selectPage(ContactApply contactApply, Integer pageSize, Integer pageNum) {
		PageVO<ContactApply> pageVO = new PageVO<ContactApply>();
		pageVO.setTotalSize(count(contactApply));
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count(contactApply) + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(contactApplyMapper.selectPage(contactApply, (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}


	@Override
	public void updateById(Integer id, ContactApply contactApply) {
		contactApplyMapper.updateById(id, contactApply);
	}

	@Override
	public void deleteById(Integer id) {
		contactApplyMapper.deleteById(id);
	}

	@Override
	public ContactApply selectById(Integer id) {
		return contactApplyMapper.selectById(id);
	}
}
