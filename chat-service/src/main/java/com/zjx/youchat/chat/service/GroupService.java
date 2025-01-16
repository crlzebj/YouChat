package com.zjx.youchat.chat.service;

import com.zjx.youchat.chat.domain.dto.GroupRegisterDTO;
import com.zjx.youchat.chat.domain.po.Group;
import com.zjx.youchat.chat.domain.vo.PageVO;

import java.util.List;

public interface GroupService {
	void insert(Group group);

	List<Group> select();
	List<Group> select(Group group);

	Integer count();
	Integer count(Group group);

	PageVO<Group> selectPage(Integer pageSize, Integer pageNum);
	PageVO<Group> selectPage(Group group, Integer pageSize, Integer pageNum);

	void updateById(String id, Group group);

	void deleteById(String id);

	Group selectById(String id);

	void register(GroupRegisterDTO groupRegisterDTO);
}
