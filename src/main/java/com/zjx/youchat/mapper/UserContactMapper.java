package com.zjx.youchat.mapper;

import com.zjx.youchat.pojo.po.UserContact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserContactMapper {
	void insert(UserContact userContact);

	List<UserContact> select(UserContact userContact);

	Integer count(UserContact userContact);

	List<UserContact> selectPage(@Param("userContact") UserContact userContact, @Param("offset") Integer offset, @Param("count") Integer count);

	void updateByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId, @Param("userContact") UserContact userContact);

	void deleteByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId);

	UserContact selectByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId);

	List<UserContact> selectByUserIdOrContactId(@Param("userId") String userId, @Param("contactId") String contactId);
}
