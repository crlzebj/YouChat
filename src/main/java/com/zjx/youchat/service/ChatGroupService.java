package com.zjx.youchat.service;

import com.zjx.youchat.pojo.dto.ChatGroupRegisterDTO;
import com.zjx.youchat.pojo.po.ChatGroup;
import com.zjx.youchat.pojo.vo.PageVO;

import java.util.List;

public interface ChatGroupService {
	void insert(ChatGroup chatGroup);

	List<ChatGroup> select();
	List<ChatGroup> select(ChatGroup chatGroup);

	Integer count();
	Integer count(ChatGroup chatGroup);

	PageVO<ChatGroup> selectPage(Integer pageSize, Integer pageNum);
	PageVO<ChatGroup> selectPage(ChatGroup chatGroup, Integer pageSize, Integer pageNum);

	void updateById(String id, ChatGroup chatGroup);

	void deleteById(String id);

	ChatGroup selectById(String id);

	void register(String ownerId, String ownerNickname, ChatGroupRegisterDTO chatGroupRegisterDTO);
}
