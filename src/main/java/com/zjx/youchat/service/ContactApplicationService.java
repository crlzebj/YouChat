package com.zjx.youchat.service;

import com.zjx.youchat.pojo.po.ContactApplication;
import com.zjx.youchat.pojo.vo.PageVO;

import java.util.List;

public interface ContactApplicationService {
	void insert(ContactApplication contactApplication);

	List<ContactApplication> select();
	List<ContactApplication> select(ContactApplication contactApplication);

	Integer count();
	Integer count(ContactApplication contactApplication);

	PageVO<ContactApplication> selectPage(Integer pageSize, Integer pageNum);
	PageVO<ContactApplication> selectPage(ContactApplication contactApplication, Integer pageSize, Integer pageNum);

	void updateById(Integer id, ContactApplication contactApplication);

	void deleteById(Integer id);

	ContactApplication selectById(Integer id);
}
