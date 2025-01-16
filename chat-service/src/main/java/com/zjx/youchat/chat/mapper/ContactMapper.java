package com.zjx.youchat.chat.mapper;

import com.zjx.youchat.chat.domain.po.Contact;
import com.zjx.youchat.chat.domain.po.GroupContact;
import com.zjx.youchat.chat.domain.po.UserContact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContactMapper {
	void insert(Contact contact);

	List<Contact> select(Contact contact);

	Integer count(Contact contact);

	List<Contact> selectPage(@Param("contact") Contact contact, @Param("offset") Integer offset, @Param("count") Integer count);

	void updateByInitiatorIdAndAccepterId(@Param("initiatorId") String initiatorId, @Param("accepterId") String accepterId, @Param("contact") Contact contact);

	void deleteByInitiatorIdAndAccepterId(@Param("initiatorId") String initiatorId, @Param("accepterId") String accepterId);

	Contact selectByInitiatorIdAndAccepterId(@Param("initiatorId") String initiatorId, @Param("accepterId") String accepterId);

	List<UserContact> queryUserContactByInitiatorId(@Param("initiatorId") String initiatorId);

	List<UserContact> queryUserContactByAccepterId(@Param("accepterId") String accepterId);

	List<GroupContact> queryGroupContactByInitiatorId(@Param("initiatorId") String initiatorId);

	List<GroupContact> queryGroupContactByAccepterId(@Param("accepterId") String accepterId);
}
