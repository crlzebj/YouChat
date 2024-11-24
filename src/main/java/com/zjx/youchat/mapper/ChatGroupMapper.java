package com.zjx.youchat.mapper;

import com.zjx.youchat.pojo.po.ChatGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatGroupMapper {
	void insert(ChatGroup chatGroup);

	List<ChatGroup> select(ChatGroup chatGroup);

	Integer count(ChatGroup chatGroup);

	List<ChatGroup> selectPage(@Param("chatGroup") ChatGroup chatGroup, @Param("offset") Integer offset, @Param("count") Integer count);

	void updateById(@Param("id") String id, @Param("chatGroup") ChatGroup chatGroup);

	void deleteById(@Param("id") String id);

	ChatGroup selectById(@Param("id") String id);
}
