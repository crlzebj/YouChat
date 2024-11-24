package com.zjx.youchat.service;

import com.zjx.youchat.pojo.po.ContactApply;
import com.zjx.youchat.pojo.vo.PageVO;

import java.util.List;

public interface ContactApplyService {
	void insert(ContactApply contactApply);

	List<ContactApply> select();
	List<ContactApply> select(ContactApply contactApply);

	Integer count();
	Integer count(ContactApply contactApply);

	PageVO<ContactApply> selectPage(Integer pageSize, Integer pageNum);
	PageVO<ContactApply> selectPage(ContactApply contactApply, Integer pageSize, Integer pageNum);

	void updateById(Integer id, ContactApply contactApply);

	void deleteById(Integer id);

	ContactApply selectById(Integer id);
}
