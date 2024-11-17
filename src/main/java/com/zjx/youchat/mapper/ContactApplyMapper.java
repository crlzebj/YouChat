package com.zjx.youchat.mapper;

import com.zjx.youchat.pojo.po.ContactApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContactApplyMapper {
	void insertContactApply(ContactApply contactApply);

	List<ContactApply> selectContactApply();

	void updateContactApplyById(@Param("id") Integer id, @Param("contactApply") ContactApply contactApply);

	void deleteContactApplyById(@Param("id") Integer id);

	ContactApply selectContactApplyById(@Param("id") Integer id);
}
