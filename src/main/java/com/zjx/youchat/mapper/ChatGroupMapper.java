package com.zjx.youchat.mapper;

import com.zjx.youchat.pojo.po.ChatGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatGroupMapper {
	void insertChatGroup(ChatGroup chatGroup);

	List<ChatGroup> selectChatGroup();

	void updateChatGroupById(@Param("id") String id, @Param("chatGroup") ChatGroup chatGroup);

	void deleteChatGroupById(@Param("id") String id);

	ChatGroup selectChatGroupById(@Param("id") String id);
}
