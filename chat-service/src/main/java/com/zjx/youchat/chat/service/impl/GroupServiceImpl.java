package com.zjx.youchat.chat.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.zjx.youchat.chat.exception.BusinessException;
import com.zjx.youchat.chat.mapper.ContactMapper;
import com.zjx.youchat.chat.mapper.GroupMapper;
import com.zjx.youchat.chat.mapper.SessionMapper;
import com.zjx.youchat.chat.domain.dto.GroupRegisterDTO;
import com.zjx.youchat.chat.domain.po.Contact;
import com.zjx.youchat.chat.domain.po.Group;
import com.zjx.youchat.chat.domain.po.Session;
import com.zjx.youchat.chat.domain.vo.PageVO;
import com.zjx.youchat.chat.service.GroupService;
import com.zjx.youchat.chat.util.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GroupServiceImpl implements GroupService {
	@Value("${you-chat.server.data-path}")
	private String dataPath;

	private final GroupMapper groupMapper;

	private final ContactMapper contactMapper;

	private final SessionMapper sessionMapper;

	@Override
	public void insert(Group group) {
		groupMapper.insert(group);
	}

	@Override
	public List<Group> select() {
		return groupMapper.select(new Group());
	}

	@Override
	public List<Group> select(Group group) {
		return groupMapper.select(group);
	}

	@Override
	public Integer count() {
		return groupMapper.count(new Group());
	}

	@Override
	public Integer count(Group group) {
		return groupMapper.count(group);
	}

	@Override
	public PageVO<Group> selectPage(Integer pageSize, Integer pageNum) {
		PageVO<Group> pageVO = new PageVO<>();
		pageVO.setTotalSize(count());
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count() + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(groupMapper.selectPage(new Group(), (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public PageVO<Group> selectPage(Group group, Integer pageSize, Integer pageNum) {
		PageVO<Group> pageVO = new PageVO<>();
		pageVO.setTotalSize(count(group));
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count(group) + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(groupMapper.selectPage(group, (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}


	@Override
	public void updateById(String id, Group group) {
		groupMapper.updateById(id, group);
	}

	@Override
	public void deleteById(String id) {
		groupMapper.deleteById(id);
	}

	@Override
	public Group selectById(String id) {
		return groupMapper.selectById(id);
	}

	@Override
	@Transactional
	public void register(GroupRegisterDTO groupRegisterDTO) {
		// 数据库中插入新群组记录
		Group group = new Group();
		// 为新群组创建id
		String id = RandomStringUtils.random(8, false, true);
		while (selectById(id) != null) {
			id = RandomStringUtils.random(8, false, true);
		}
		group.setId(id);
		group.setNickname(groupRegisterDTO.getNickname());
		group.setOwnerId(ThreadLocalUtil.getUserId());
		group.setNotice(groupRegisterDTO.getNotice());
		group.setStatus(1);
		group.setAuthority(groupRegisterDTO.getAuthority());
		group.setCreateTime(LocalDateTime.now());
		try {
			Path avatarPath = Paths.get(dataPath, "avatar");
			if (!Files.exists(avatarPath)) {
				Files.createDirectories(avatarPath);
			}
			File file1 = new File(avatarPath.toString(), group.getId() + ".png");
			groupRegisterDTO.getAvatarFile().transferTo(file1.getAbsoluteFile());

			Path avatarThumbnailPath = Paths.get(dataPath, "avatarThumbnail");
			if (!Files.exists(avatarThumbnailPath)) {
				Files.createDirectories(avatarThumbnailPath);
			}
			File file2 = new File(avatarThumbnailPath.toString(), group.getId() + ".png");
			groupRegisterDTO.getAvatarThumbnailFile().transferTo(file2.getAbsoluteFile());
		} catch (Exception e) {
			throw new BusinessException("头像上传失败，请稍后重试");
		}
		groupMapper.insert(group);

		/*
			将新群组加入群主好友列表
			为新群组创建会话窗口
		 */
		Contact contact = new Contact();
		contact.setInitiatorId(ThreadLocalUtil.getUserId());
		contact.setAccepterId(group.getId());
		contact.setStatus(0);
		contact.setContactType(1);
		contact.setCreateTime(LocalDateTime.now());
		contact.setLastUpdateTime(LocalDateTime.now());
		contactMapper.insert(contact);

		Session session = new Session();
		session.setInitiatorId(group.getId());
		session.setAccepterId(group.getId());
		String sessionId = group.getId();
		session.setId(DigestUtil.md5Hex(sessionId.getBytes()));
		session.setLastMessage(null);
		session.setLastSendTime(null);
		sessionMapper.insert(session);

		// TODO发送websocket消息
	}
}
