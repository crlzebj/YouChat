package com.zjx.youchat.mapper;

import com.zjx.youchat.pojo.po.Session;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SessionMapper {
	void insert(Session session);

	List<Session> select(Session session);

	Integer count(Session session);

	List<Session> selectPage(@Param("session") Session session, @Param("offset") Integer offset, @Param("count") Integer count);

	void updateByInitiatorIdAndAccepterId(@Param("initiatorId") String initiatorId, @Param("accepterId") String accepterId, @Param("session") Session session);

	void deleteByInitiatorIdAndAccepterId(@Param("initiatorId") String initiatorId, @Param("accepterId") String accepterId);

	Session selectByInitiatorIdAndAccepterId(@Param("initiatorId") String initiatorId, @Param("accepterId") String accepterId);

	void updateById(@Param("id") String id, @Param("session") Session session);

	void deleteById(@Param("id") String id);

	Session selectById(@Param("id") String id);

	List<Session> selectByInitiatorIdOrAccepterId(@Param("initiatorId") String initiatorId,
												  @Param("accepterId") String accepterId);
}
