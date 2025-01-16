package com.zjx.youchat.chat.mapper;

import com.zjx.youchat.chat.domain.po.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
	void insert(Message message);

	List<Message> select(Message message);

	Integer count(Message message);

	List<Message> selectPage(@Param("message") Message message, @Param("offset") Integer offset, @Param("count") Integer count);

	void updateById(@Param("id") Long id, @Param("message") Message message);

	void deleteById(@Param("id") Long id);

	Message selectById(@Param("id") Long id);
}
