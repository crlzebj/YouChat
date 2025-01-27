package com.zjx.youchat.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.zjx.youchat.chat.constant.ExceptionConstant;
import com.zjx.youchat.chat.constant.UserConstant;
import com.zjx.youchat.chat.domain.po.Contact;
import com.zjx.youchat.chat.exception.BusinessException;
import com.zjx.youchat.chat.mapper.ContactApplyMapper;
import com.zjx.youchat.chat.mapper.ContactMapper;
import com.zjx.youchat.chat.mapper.GroupMapper;
import com.zjx.youchat.chat.mapper.UserMapper;
import com.zjx.youchat.chat.domain.dto.ContactApplyDTO;
import com.zjx.youchat.chat.domain.po.ContactApply;
import com.zjx.youchat.chat.domain.po.Group;
import com.zjx.youchat.chat.domain.po.User;
import com.zjx.youchat.chat.domain.vo.GroupQueryVO;
import com.zjx.youchat.chat.domain.vo.PageVO;
import com.zjx.youchat.chat.domain.vo.UserQueryVO;
import com.zjx.youchat.chat.service.ContactApplyService;
import com.zjx.youchat.chat.util.RedisUtil;
import com.zjx.youchat.chat.util.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ContactApplyServiceImpl implements ContactApplyService {
	private final ContactApplyMapper contactApplyMapper;

	private final UserMapper userMapper;

	private final GroupMapper groupMapper;

	private final ContactMapper contactMapper;

	private final RedisUtil redisUtil;

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
		PageVO<ContactApply> pageVO = new PageVO<>();
		pageVO.setTotalSize(count());
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count() + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(contactApplyMapper.selectPage(new ContactApply(), (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public PageVO<ContactApply> selectPage(ContactApply contactApply, Integer pageSize, Integer pageNum) {
		PageVO<ContactApply> pageVO = new PageVO<>();
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
	public void apply(ContactApplyDTO contactApplyDTO) {
		String accepterId = null;
		Integer authority = null;
		if (contactApplyDTO.getApplyType() == 0) {
			User user = userMapper.selectById(contactApplyDTO.getContactId());
			if (user == null) {
				throw new BusinessException(ExceptionConstant.USER_NOT_EXIST);
			}
			accepterId = user.getId();
			authority = user.getAuthority();
		} else if (contactApplyDTO.getApplyType() == 1) {
			Group group = groupMapper.selectById(contactApplyDTO.getContactId());
			if (group == null) {
				throw new BusinessException(ExceptionConstant.GROUP_NOT_EXIST);
			}
			accepterId = group.getOwnerId();
			authority = group.getAuthority();
		} else {
			throw new BusinessException(ExceptionConstant.ILLEGAL_REQUEST);
		}

		if (contactMapper.selectByInitiatorIdAndAccepterId(ThreadLocalUtil.getUserId(),
				contactApplyDTO.getContactId()) != null ||
				contactMapper.selectByInitiatorIdAndAccepterId(contactApplyDTO.getContactId(),
						ThreadLocalUtil.getUserId()) != null) {
			throw new BusinessException(ExceptionConstant.ALREADY_BE_CONTACTS);
		}

		// 判断用户或群组的权限
		if (authority == 0) {
			throw new BusinessException(ExceptionConstant.ILLEGAL_REQUEST);
		}
		ContactApply contactApply = new ContactApply();
		BeanUtil.copyProperties(contactApplyDTO, contactApply);
		contactApply.setId(redisUtil.generateId(UserConstant.CONTACT_APPLY_ID_PREFIX));
		contactApply.setInitiatorId(ThreadLocalUtil.getUserId());
		contactApply.setAccepterId(accepterId);
		contactApply.setLastApplyTime(LocalDateTime.now());
		contactApply.setStatus(authority == 2 ? 1 : 0);
		contactApplyMapper.insert(contactApply);

		// TODO发送websocket消息

		if (authority == 2) {
			// 数据库中插入联系人
			Contact contact = new Contact();
			contact.setInitiatorId(contactApply.getInitiatorId());
			contact.setAccepterId(contactApply.getContactId());
			contact.setStatus(0);
			contact.setContactType(contactApply.getApplyType());
			contact.setCreateTime(LocalDateTime.now());
			contact.setLastUpdateTime(LocalDateTime.now());
			contactMapper.insert(contact);

			// TODO发送websocket消息
		}
	}

	@Override
	@Transactional
	public void accept(Long id) {
		ContactApply contactApply = contactApplyMapper.selectById(id);

		// 校验好友申请信息
		// 判断数据库中是否有改联系人申请以及是否为发送给自己的好友申请
		if (contactApply == null || !contactApply.getAccepterId().equals(ThreadLocalUtil.getUserId())
				|| contactApply.getStatus() == 1) {
			throw new BusinessException(ExceptionConstant.ILLEGAL_REQUEST);
		}

		// TODO数据库新增会话

		// 数据库中插入联系人
		Contact contact = new Contact();
		contact.setInitiatorId(contactApply.getInitiatorId());
		contact.setAccepterId(contactApply.getContactId());
		contact.setStatus(0);
		contact.setContactType(contactApply.getApplyType());
		contact.setCreateTime(LocalDateTime.now());
		contact.setLastUpdateTime(LocalDateTime.now());
		contactMapper.insert(contact);

		// 修改联系人申请处理状态
		contactApply.setStatus(1);
		contactApplyMapper.updateById(id, contactApply);



		// TODO发送websocket消息
	}
}
