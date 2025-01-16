package com.zjx.youchat.chat.mapper;

import com.zjx.youchat.chat.domain.po.ContactApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContactApplyMapper {
	void insert(ContactApply contactApply);

	List<ContactApply> select(ContactApply contactApply);

	Integer count(ContactApply contactApply);

	List<ContactApply> selectPage(@Param("contactApply") ContactApply contactApply, @Param("offset") Integer offset, @Param("count") Integer count);

	void updateById(@Param("id") Long id, @Param("contactApply") ContactApply contactApply);

	void deleteById(@Param("id") Long id);

	ContactApply selectById(@Param("id") Long id);
}
