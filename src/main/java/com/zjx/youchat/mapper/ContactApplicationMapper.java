package com.zjx.youchat.mapper;

import com.zjx.youchat.pojo.po.ContactApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContactApplicationMapper {
	void insert(ContactApplication contactApplication);

	List<ContactApplication> select(ContactApplication contactApplication);

	Integer count(ContactApplication contactApplication);

	List<ContactApplication> selectPage(@Param("contactApplication") ContactApplication contactApplication, @Param("offset") Integer offset, @Param("count") Integer count);

	void updateById(@Param("id") Integer id, @Param("contactApplication") ContactApplication contactApplication);

	void deleteById(@Param("id") Integer id);

	ContactApplication selectById(@Param("id") Integer id);
}
