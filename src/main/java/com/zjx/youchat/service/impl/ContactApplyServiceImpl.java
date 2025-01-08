package com.zjx.youchat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.zjx.youchat.constant.ExceptionConstant;
import com.zjx.youchat.constant.UserConstant;
import com.zjx.youchat.exception.BusinessException;
import com.zjx.youchat.mapper.ContactApplyMapper;
import com.zjx.youchat.mapper.ContactMapper;
import com.zjx.youchat.mapper.GroupMapper;
import com.zjx.youchat.mapper.UserMapper;
import com.zjx.youchat.pojo.dto.ContactApplyAddDTO;
import com.zjx.youchat.pojo.po.ContactApply;
import com.zjx.youchat.pojo.po.Group;
import com.zjx.youchat.pojo.po.User;
import com.zjx.youchat.pojo.vo.GroupQueryVO;
import com.zjx.youchat.pojo.vo.PageVO;
import com.zjx.youchat.pojo.vo.UserQueryVO;
import com.zjx.youchat.service.ContactApplyService;
import com.zjx.youchat.util.RedisUtil;
import com.zjx.youchat.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactApplyServiceImpl implements ContactApplyService {
	@Autowired
	private ContactApplyMapper contactApplyMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private GroupMapper groupMapper;

	@Autowired
	private ContactMapper contactMapper;

	@Autowired
	private RedisUtil redisUtil;

	@Override
	public void insert(ContactApply contactApply) {
		contactApplyMapper.insert(contactApply);
	}

	@Override
	public List<ContactApply> select() {
		return contactApplyMapper.select(new ContactApply());
	}

	@Override
	public List<ContactApply> select(ContactApply contactApply) {
		return contactApplyMapper.select(contactApply);
	}

	@Override
	public Integer count() {
		return contactApplyMapper.count(new ContactApply());
	}

	@Override
	public Integer count(ContactApply contactApply) {
		return contactApplyMapper.count(contactApply);
	}

	@Override
	public PageVO<ContactApply> selectPage(Integer pageSize, Integer pageNum) {
		PageVO<ContactApply> pageVO = new PageVO<ContactApply>();
		pageVO.setTotalSize(count());
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count() + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(contactApplyMapper.selectPage(new ContactApply(), (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public PageVO<ContactApply> selectPage(ContactApply contactApply, Integer pageSize, Integer pageNum) {
		PageVO<ContactApply> pageVO = new PageVO<ContactApply>();
		pageVO.setTotalSize(count(contactApply));
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count(contactApply) + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(contactApplyMapper.selectPage(contactApply, (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public void updateById(Long id, ContactApply contactApply) {
		contactApplyMapper.updateById(id, contactApply);
	}

	@Override
	public void deleteById(Long id) {
		contactApplyMapper.deleteById(id);
	}

	@Override
	public ContactApply selectById(Long id) {
		return contactApplyMapper.selectById(id);
	}

	@Override
	public UserQueryVO queryUser(String userId) {
		User user = userMapper.selectById(userId);
		if (user == null) {
			throw new BusinessException(ExceptionConstant.USER_NOT_EXIST);
		}
		UserQueryVO userQueryVO = new UserQueryVO();
		BeanUtil.copyProperties(user, userQueryVO);
		return userQueryVO;
	}

	@Override
	public GroupQueryVO queryGroup(String groupId) {
		Group group = groupMapper.selectById(groupId);
		if (group == null) {
			throw new BusinessException(ExceptionConstant.GROUP_NOT_EXIST);
		}
		GroupQueryVO groupQueryVO = new GroupQueryVO();
		BeanUtil.copyProperties(group, groupQueryVO);
		return groupQueryVO;
	}

	@Override
	@Transactional
	public void add(ContactApplyAddDTO contactApplyAddDTO) {
		String accepterId = null;
		Integer authority = null;
		if (contactApplyAddDTO.getApplyType() == 0) {
			User user = userMapper.selectById(contactApplyAddDTO.getContactId());
			if (user == null) {
				throw new BusinessException(ExceptionConstant.USER_NOT_EXIST);
			}
			accepterId = user.getId();
			authority = user.getAuthority();
		} else if (contactApplyAddDTO.getApplyType() == 1) {
			Group group = groupMapper.selectById(contactApplyAddDTO.getContactId());
			if (group == null) {
				throw new BusinessException(ExceptionConstant.GROUP_NOT_EXIST);
			}
			accepterId = group.getOwnerId();
			authority = group.getAuthority();
		} else {
			throw new BusinessException(ExceptionConstant.ILLEGAL_REQUEST);
		}

		if (contactMapper.selectByInitiatorIdAndAccepterId(ThreadLocalUtil.getUserId(),
				contactApplyAddDTO.getContactId()) != null ||
				contactMapper.selectByInitiatorIdAndAccepterId(contactApplyAddDTO.getContactId(),
						ThreadLocalUtil.getUserId()) != null) {
			throw new BusinessException(ExceptionConstant.ALREADY_BE_CONTACTS);
		}

		// 判断用户或群组的权限
		if (authority == 0) {
			throw new BusinessException(ExceptionConstant.ILLEGAL_REQUEST);
		}
		ContactApply contactApply = new ContactApply();
		BeanUtil.copyProperties(contactApplyAddDTO, contactApply);
		contactApply.setId(redisUtil.generateId(UserConstant.CONTACT_APPLY_ID_PREFIX));
		contactApply.setInitiatorId(ThreadLocalUtil.getUserId());
		contactApply.setAccepterId(accepterId);
		contactApply.setLastApplyTime(LocalDateTime.now());
		contactApply.setStatus(authority == 2 ? 1 : 0);
		contactApplyMapper.insert(contactApply);

		// TODO发送websocket消息

		// TODO不需要验证信息的用户或群组直接添加好友
		if (authority == 1) {
			return;
		}
	}
}
