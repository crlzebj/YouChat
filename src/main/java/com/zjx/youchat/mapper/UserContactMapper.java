package com.zjx.youchat.mapper;

import com.zjx.youchat.pojo.po.UserContact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserContactMapper {
	void insertUserContact(UserContact userContact);

	List<UserContact> selectUserContact();

	void updateUserContactByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId, @Param("userContact") UserContact userContact);

	void deleteUserContactByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId);

	UserContact selectUserContactByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId);
}
