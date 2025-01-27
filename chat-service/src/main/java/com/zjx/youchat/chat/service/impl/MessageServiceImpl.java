package com.zjx.youchat.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.zjx.youchat.api.client.URIClient;
import com.zjx.youchat.chat.constant.ExceptionConstant;
import com.zjx.youchat.chat.constant.UserConstant;
import com.zjx.youchat.chat.domain.dto.MessageSendDTO;
import com.zjx.youchat.chat.domain.dto.WSPackage;
import com.zjx.youchat.chat.exception.BusinessException;
import com.zjx.youchat.chat.mapper.ContactMapper;
import com.zjx.youchat.chat.mapper.MessageMapper;
import com.zjx.youchat.chat.domain.po.Message;
import com.zjx.youchat.chat.domain.vo.PageVO;
import com.zjx.youchat.chat.mapper.SessionMapper;
import com.zjx.youchat.chat.service.MessageService;
import com.zjx.youchat.chat.util.RedisUtil;
import com.zjx.youchat.chat.util.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {
	private final MessageMapper messageMapper;

	private final SessionMapper sessionMapper;

	private final RedisUtil redisUtil;

	private final ContactMapper contactMapper;

	private final URIClient uriClient;

	private final RestTemplate restTemplate;

	private final DiscoveryClient discoveryClient;

	@Override
	public void insert(Message message) {
		messageMapper.insert(message);
	}

	@Override
	public List<Message> select() {
		return messageMapper.select(new Message());
	}

	@Override
	public List<Message> select(Message message) {
		return messageMapper.select(message);
	}

	@Override
	public Integer count() {
		return messageMapper.count(new Message());
	}

	@Override
	public Integer count(Message message) {
		return messageMapper.count(message);
	}

	@Override
	public PageVO<Message> selectPage(Integer pageSize, Integer pageNum) {
		PageVO<Message> pageVO = new PageVO<Message>();
		pageVO.setTotalSize(count());
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count() + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(messageMapper.selectPage(new Message(), (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public PageVO<Message> selectPage(Message message, Integer pageSize, Integer pageNum) {
		PageVO<Message> pageVO = new PageVO<Message>();
		pageVO.setTotalSize(count(message));
		pageVO.setPageSize(pageSize);
		pageVO.setTotalPage((count(message) + pageSize - 1) / pageSize);
		pageVO.setPageNum(pageNum);
		pageVO.setList(messageMapper.selectPage(message, (pageNum - 1) * pageSize, pageSize));
		return pageVO;
	}

	@Override
	public void updateById(Long id, Message message) {
		messageMapper.updateById(id, message);
	}

	@Override
	public void deleteById(Long id) {
		messageMapper.deleteById(id);
	}

	@Override
	public Message selectById(Long id) {
		return messageMapper.selectById(id);
	}

	@Override
	public List<Message> getMyMessage() {
		String userId = ThreadLocalUtil.getUserId();
		List<String> sessionIds = sessionMapper.selectSessionId(userId);
		return messageMapper.selectBySessionIds(sessionIds);
	}

	@Override
	public void send(MessageSendDTO messageSendDTO) {
		String senderId = ThreadLocalUtil.getUserId();
		// 校验好友关系
		if (contactMapper.selectByInitiatorIdAndAccepterId(senderId,
				messageSendDTO.getReceiverId()) == null &&
				contactMapper.selectByInitiatorIdAndAccepterId(messageSendDTO.getReceiverId(),
						senderId) == null) {
			throw new BusinessException(ExceptionConstant.ILLEGAL_REQUEST1.formatted(senderId));
		}

		// TODO消息队列异步修改数据库
		Message message = new Message();
		BeanUtil.copyProperties(messageSendDTO, message);
		Long messageId = redisUtil.generateId(UserConstant.MESSAGE_ID_PREFIX);
		message.setId(messageId);
		message.setSenderId(ThreadLocalUtil.getUserId());
		String sessionId = null;
		if (message.getReceiverType() == 0) {
			sessionId = message.getSenderId().compareTo(message.getReceiverId()) > 0 ?
					DigestUtil.md5Hex((message.getSenderId() + message.getReceiverId()).getBytes()) :
					DigestUtil.md5Hex((message.getReceiverId() + message.getSenderId()).getBytes());
		} else if (message.getReceiverType() == 1) {
			sessionId = DigestUtil.md5Hex(message.getReceiverId().getBytes());
		}
		message.setSessionId(sessionId);
		message.setSendTime(LocalDateTime.now());
		messageMapper.insert(message);

		// TODO发送websocket消息
		if (messageSendDTO.getReceiverType() == 0) {
			String uri = uriClient.queryUser(message.getReceiverId());

			WSPackage<Message> wsPackage = new WSPackage<>();
			wsPackage.setType(1);
			wsPackage.setReceiverId(messageSendDTO.getReceiverId());
			wsPackage.setData(message);

			HttpEntity<WSPackage<Message>> httpEntity = new HttpEntity<>(wsPackage);
			// 发送请求获得响应
			ResponseEntity<Void> response = restTemplate.exchange(
					"http://" + uri + "/msg/user",
					HttpMethod.POST,
					httpEntity,
					Void.class
			);
		} else {
			List<String> uris = uriClient.queryGroup(message.getReceiverId());
			WSPackage<Message> wsPackage = new WSPackage<>();
			wsPackage.setType(2);
			wsPackage.setReceiverId(messageSendDTO.getReceiverId());
			wsPackage.setData(message);

			HttpEntity<WSPackage<Message>> httpEntity = new HttpEntity<>(wsPackage);
			for (String uri : uris) {
				// 发送请求获得响应
				ResponseEntity<Void> response = restTemplate.exchange(
						"http://" + uri + "/msg/group",
						HttpMethod.POST,
						httpEntity,
						Void.class
				);
			}
		}
	}
}
