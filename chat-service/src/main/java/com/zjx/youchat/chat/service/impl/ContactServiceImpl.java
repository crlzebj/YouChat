package com.zjx.youchat.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.zjx.youchat.chat.constant.ExceptionConstant;
import com.zjx.youchat.chat.exception.BusinessException;
import com.zjx.youchat.chat.mapper.ContactApplyMapper;
import com.zjx.youchat.chat.mapper.ContactMapper;
import com.zjx.youchat.chat.mapper.GroupMapper;
import com.zjx.youchat.chat.mapper.UserMapper;
import com.zjx.youchat.chat.domain.po.Contact;
import com.zjx.youchat.chat.domain.po.ContactApply;
import com.zjx.youchat.chat.domain.po.Group;
import com.zjx.youchat.chat.domain.po.User;
import com.zjx.youchat.chat.domain.vo.PageVO;
import com.zjx.youchat.chat.domain.vo.UserViewVO;
import com.zjx.youchat.chat.service.ContactService;
import com.zjx.youchat.chat.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
	@Autowired
	private ContactMapper contactMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private GroupMapper groupMapper;
    @Autowired
    private ContactApplyMapper contactApplyMapper;

	@Override
	public void insert(Contact contact) {
		contactMapper.insert(contact);
	}

	@Override
	public List<Contact> select() {
		return contactMapper.select(new Contact());
	}

	@Override
	public List<Contact> select(Contact contact) {
		return contactMapper.select(contact);
	}

	@Override
	public Integer count() {
		return contactMapper.count(new Contact());
	}

	@Override
	public Integer count(Contact contact) {
		return contactMapper.count(contact);
	}

	@Override
	public PageVO<Contact> selectPage(Integer pageSize, Integer pageNum) {
		PageVO<Contact> pageVO = new PageVO<Contact>();
		pageVO.setTotalSize(count());
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count() + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(contactMapper.selectPage(new Contact(), (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public PageVO<Contact> selectPage(Contact contact, Integer pageSize, Integer pageNum) {
		PageVO<Contact> pageVO = new PageVO<Contact>();
		pageVO.setTotalSize(count(contact));
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count(contact) + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(contactMapper.selectPage(contact, (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}


	@Override
	public void updateByInitiatorIdAndAccepterId(String initiatorId, String accepterId, Contact contact) {
		contactMapper.updateByInitiatorIdAndAccepterId(initiatorId, accepterId, contact);
	}

	@Override
	public void deleteByInitiatorIdAndAccepterId(String initiatorId, String accepterId) {
		contactMapper.deleteByInitiatorIdAndAccepterId(initiatorId, accepterId);
	}

	@Override
	public Contact selectByInitiatorIdAndAccepterId(String initiatorId, String accepterId) {
		return contactMapper.selectByInitiatorIdAndAccepterId(initiatorId, accepterId);
	}

	@Override
	public UserViewVO viewUser(String userId) {
		User user = userMapper.selectById(userId);
		if (user == null) {
			throw new BusinessException(ExceptionConstant.USER_NOT_EXIST);
		}
		// TODO判断用户是否有该好友
		UserViewVO userViewVO = new UserViewVO();
		BeanUtil.copyProperties(user, userViewVO);
		return userViewVO;
	}

	@Override
	public Group viewGroup(String groupId) {
		Group group = groupMapper.selectById(groupId);
		if (group == null) {
			throw new BusinessException(ExceptionConstant.GROUP_NOT_EXIST);
		}
		// TODO校验好友关系
		return group;
	}

	@Override
	@Transactional
	public void accept(Long contactId) {
		ContactApply contactApply = contactApplyMapper.selectById(contactId);

		// 校验好友申请信息
		// 判断数据库中是否有改联系人申请以及是否为发送给自己的好友申请
		if (contactApply == null || !contactApply.getAccepterId().equals(ThreadLocalUtil.getUserId())) {
			throw new BusinessException(ExceptionConstant.ILLEGAL_REQUEST);
		}

		// 数据库中插入联系人
		Contact contact = new Contact();
		contact.setInitiatorId(contactApply.getInitiatorId());
		contact.setAccepterId(contactApply.getContactId());
		contact.setStatus(0);
		contact.setContactType(contactApply.getApplyType());
		contact.setCreateTime(LocalDateTime.now());
		contact.setLastUpdateTime(LocalDateTime.now());
		contactMapper.insert(contact);

		// TODO修改联系人申请处理状态
	}
}
