package com.zjx.youchat.service.impl;

import com.zjx.youchat.mapper.UserContactMapper;
import com.zjx.youchat.pojo.po.UserContact;
import com.zjx.youchat.pojo.vo.PageVO;

import com.zjx.youchat.service.UserContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserContactServiceImpl implements UserContactService {
	@Autowired
	private UserContactMapper userContactMapper;

	@Override
	public void insert(UserContact userContact) {
		userContactMapper.insert(userContact);
	}

	@Override
	public List<UserContact> select() {
		return userContactMapper.select(new UserContact());
	}

	@Override
	public List<UserContact> select(UserContact userContact) {
		return userContactMapper.select(userContact);
	}

	@Override
	public Integer count() {
		return userContactMapper.count(new UserContact());
	}

	@Override
	public Integer count(UserContact userContact) {
		return userContactMapper.count(userContact);
	}

	@Override
	public PageVO<UserContact> selectPage(Integer pageSize, Integer pageNum) {
		PageVO<UserContact> pageVO = new PageVO<UserContact>();
		pageVO.setTotalSize(count());
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count() + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(userContactMapper.selectPage(new UserContact(), (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public PageVO<UserContact> selectPage(UserContact userContact, Integer pageSize, Integer pageNum) {
		PageVO<UserContact> pageVO = new PageVO<UserContact>();
		pageVO.setTotalSize(count(userContact));
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count(userContact) + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(userContactMapper.selectPage(userContact, (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}


	@Override
	public void updateByUserIdAndContactId(String userId, String contactId, UserContact userContact) {
		userContactMapper.updateByUserIdAndContactId(userId, contactId, userContact);
	}

	@Override
	public void deleteByUserIdAndContactId(String userId, String contactId) {
		userContactMapper.deleteByUserIdAndContactId(userId, contactId);
	}

	@Override
	public UserContact selectByUserIdAndContactId(String userId, String contactId) {
		return userContactMapper.selectByUserIdAndContactId(userId, contactId);
	}
}
