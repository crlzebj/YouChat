package com.zjx.youchat.service;

import com.zjx.youchat.pojo.dto.ContactApplyAddDTO;
import com.zjx.youchat.pojo.po.ContactApply;
import com.zjx.youchat.pojo.vo.GroupQueryVO;
import com.zjx.youchat.pojo.vo.PageVO;
import com.zjx.youchat.pojo.vo.UserQueryVO;

import java.util.List;

public interface ContactApplyService {
	void insert(ContactApply contactApply);

	List<ContactApply> select();
	List<ContactApply> select(ContactApply contactApply);

	Integer count();
	Integer count(ContactApply contactApply);

	PageVO<ContactApply> selectPage(Integer pageSize, Integer pageNum);
	PageVO<ContactApply> selectPage(ContactApply contactApply, Integer pageSize, Integer pageNum);

	void updateById(Long id, ContactApply contactApply);

	void deleteById(Long id);

	ContactApply selectById(Long id);

	UserQueryVO queryUser(String userId);

	GroupQueryVO queryGroup(String groupId);

	void add(ContactApplyAddDTO contactApplyAddDTO);
}
