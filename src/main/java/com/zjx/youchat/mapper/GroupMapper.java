package com.zjx.youchat.mapper;

import com.zjx.youchat.pojo.po.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupMapper {
	void insert(Group group);

	List<Group> select(Group group);

	Integer count(Group group);

	List<Group> selectPage(@Param("group") Group group, @Param("offset") Integer offset, @Param("count") Integer count);

	void updateById(@Param("id") String id, @Param("group") Group group);

	void deleteById(@Param("id") String id);

	Group selectById(@Param("id") String id);
}
