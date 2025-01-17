package com.zjx.youchat.chat.service;

import com.zjx.youchat.chat.domain.dto.ContactApplyDTO;
import com.zjx.youchat.chat.domain.po.ContactApply;
import com.zjx.youchat.chat.domain.vo.GroupQueryVO;
import com.zjx.youchat.chat.domain.vo.PageVO;
import com.zjx.youchat.chat.domain.vo.UserQueryVO;

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

	void apply(ContactApplyDTO contactApplyDTO);

	void accept(Long id);
}
