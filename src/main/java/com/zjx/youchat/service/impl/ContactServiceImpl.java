package com.zjx.youchat.service.impl;

import com.zjx.youchat.constant.WebsocketPackageConstant;
import com.zjx.youchat.exception.BusinessException;
import com.zjx.youchat.mapper.*;
import com.zjx.youchat.pojo.dto.ContactAddDTO;
import com.zjx.youchat.pojo.dto.WebsocketPackageDTO;
import com.zjx.youchat.pojo.po.*;
import com.zjx.youchat.pojo.vo.ContactSearchVO;
import com.zjx.youchat.pojo.vo.PageVO;

import com.zjx.youchat.service.ContactService;
import com.zjx.youchat.util.ThreadLocalUtil;
import com.zjx.youchat.websocket.service.RedissonService;
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
	private ChatGroupMapper chatGroupMapper;

	@Autowired
	private ContactApplicationMapper contactApplicationMapper;

    @Autowired
    private SessionMapper sessionMapper;

	@Autowired
	private RedissonService redissonService;

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
	public List<Contact> selectByInitiatorIdOrAccepterId(String initiatorId, String accepterId) {
		return contactMapper.selectByInitiatorIdOrAccepterId(initiatorId, accepterId);
	}

	@Override
	public ContactSearchVO search(String contactId) {
		Contact contact = selectByInitiatorIdAndAccepterId(ThreadLocalUtil.getId(),contactId);
		if (contact == null) {
			contact = selectByInitiatorIdAndAccepterId(contactId, ThreadLocalUtil.getId());
		}
		ContactSearchVO contactSearchVO = new ContactSearchVO();
		if (contactId.equals(ThreadLocalUtil.getId())) {
			contactSearchVO.setAddContactType(0);
		}
		contactSearchVO.setAddContactType(contact != null ? 1 : 2);
		contactSearchVO.setId(contactId);
		if (contactId.startsWith("U")) {
			contactSearchVO.setContactType(0);
			User user = userMapper.selectById(contactId);
			if (user == null) {
				throw new BusinessException("不存在该用户或群组");
			}
			contactSearchVO.setStatus(user.getStatus());
			contactSearchVO.setNickname(user.getNickname());
		} else if (contactId.startsWith("G")) {
			contactSearchVO.setContactType(1);
			ChatGroup chatGroup = chatGroupMapper.selectById(contactId);
			if (chatGroup == null) {
				throw new BusinessException("不存在该用户或群组");
			}
			contactSearchVO.setStatus(chatGroup.getStatus());
			contactSearchVO.setNickname(chatGroup.getNickname());
		} else {
			throw new BusinessException("不存在该用户或群组");
		}
		return contactSearchVO;
	}

	@Override
	@Transactional
	public void add(ContactAddDTO contactAddDTO) {
		String initiatorId = ThreadLocalUtil.getId();
		String accepterId = contactAddDTO.getContactId();
		String applicationInfo = contactAddDTO.getApplicationInfo();

		ContactApplication contactApplication = new ContactApplication();
		contactApplication.setApplicantId(initiatorId);
		contactApplication.setContactId(accepterId);
		contactApplication.setType(contactAddDTO.getContactType());
		contactApplication.setApplicationInfo(applicationInfo);

		if (contactAddDTO.getContactType() == 0) {
			if (selectByInitiatorIdAndAccepterId(initiatorId, accepterId) != null
					|| selectByInitiatorIdAndAccepterId(accepterId, initiatorId) != null) {
				throw new BusinessException("已经添加为好友");
			}
			User user = userMapper.selectById(accepterId);
			if (user == null) {
				throw new BusinessException("该用户不存在");
			}
			if (user.getStatus() == 0) {
				throw new BusinessException("该用户被封禁");
			}
			if (user.getPermission() != 0 && user.getPermission() != 1) {
				throw new BusinessException("该用户不允许被添加为好友");
			}
			contactApplication.setAccepterId(accepterId);
			if (user.getPermission() == 0) {
				contactApplication.setStatus(1);

				// 数据库中插入会话
				Session session = new Session();
				session.setInitiatorId(initiatorId);
				session.setInitiatorNickname(ThreadLocalUtil.getNickname());
				session.setAccepterId(accepterId);
				session.setAccepterNickname(user.getNickname());
				String sessionId = initiatorId.compareTo(accepterId) < 0 ?
						initiatorId + accepterId : accepterId + initiatorId;
				session.setId(sessionId);
				session.setLastReceiveTime(LocalDateTime.now());
				sessionMapper.insert(session);

				// 数据库中插入联系人
				Contact contact = new Contact();
				contact.setInitiatorId(initiatorId);
				contact.setInitiatorNickname(ThreadLocalUtil.getNickname());
				contact.setAccepterId(accepterId);
				contact.setAccepterNickname(user.getNickname());
				contact.setType(contactAddDTO.getContactType());
				contact.setStatus(0);
				contact.setCreateTime(LocalDateTime.now());
				contact.setLastUpdateTime(LocalDateTime.now());
				contactMapper.insert(contact);
			} else {
				contactApplication.setStatus(0);
				WebsocketPackageDTO<ContactApplication> websocketPackageDTO =
						new WebsocketPackageDTO<>();
				websocketPackageDTO.setReceiverId(accepterId);
				websocketPackageDTO.setType(WebsocketPackageConstant.CONTACT_APPLICATION_TYPE);
				websocketPackageDTO.setData(contactApplication);
				redissonService.publish(websocketPackageDTO);
			}
		} else if (contactAddDTO.getContactType() == 1) {
			if (selectByInitiatorIdAndAccepterId(initiatorId, accepterId) != null) {
				throw new BusinessException("已经加入群组");
			}
			ChatGroup chatGroup = chatGroupMapper.selectById(accepterId);
			if (chatGroup == null) {
				throw new BusinessException("该群组不存在");
			}
			if (chatGroup.getStatus() == 0) {
				throw new BusinessException("该群组被封禁");
			}
			if (chatGroup.getPermission() != 0 && chatGroup.getPermission() != 1) {
				throw new BusinessException("该群组禁止加入");
			}
			contactApplication.setAccepterId(accepterId);
			if (chatGroup.getPermission() == 0) {
				contactApplication.setStatus(1);

				// 数据库中插入会话
				Session session = new Session();
				session.setInitiatorId(initiatorId);
				session.setInitiatorNickname(ThreadLocalUtil.getNickname());
				session.setAccepterId(accepterId);
				session.setAccepterNickname(chatGroup.getNickname());
				String sessionId = accepterId + initiatorId;
				session.setId(sessionId);
				session.setLastReceiveTime(LocalDateTime.now());
				sessionMapper.insert(session);

				// 数据库中插入联系人
				Contact contact = new Contact();
				contact.setInitiatorId(initiatorId);
				contact.setInitiatorNickname(ThreadLocalUtil.getNickname());
				contact.setAccepterId(accepterId);
				contact.setAccepterNickname(chatGroup.getNickname());
				contact.setType(contactAddDTO.getContactType());
				contact.setStatus(0);
				contact.setCreateTime(LocalDateTime.now());
				contact.setLastUpdateTime(LocalDateTime.now());
				contactMapper.insert(contact);
			} else {
				contactApplication.setStatus(0);
			}
		} else {
			throw new BusinessException("非法请求！");
		}

		contactApplication.setLastApplicationTime(LocalDateTime.now());
		contactApplicationMapper.insert(contactApplication);
	}
}
