package com.zjx.youchat.controller;

import com.zjx.youchat.constant.UserConstant;
import com.zjx.youchat.pojo.dto.ChatGroupRegisterDTO;
import com.zjx.youchat.pojo.vo.ResponseVO;
import com.zjx.youchat.service.ChatGroupService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/chatGroups")
public class ChatGroupController {
	@Autowired
	private ChatGroupService chatGroupService;

	@PostMapping("/register")
	public ResponseVO register(HttpServletRequest request, ChatGroupRegisterDTO chatGroupRegisterDTO) {
		String token = request.getHeader("token");
		String ownerId = Jwts.parser().setSigningKey(UserConstant.SECRET_KEY).
				parseClaimsJws(token).getBody().get("id", String.class);
		String ownerNickname = Jwts.parser().setSigningKey(UserConstant.SECRET_KEY).
				parseClaimsJws(token).getBody().get("nickname", String.class);
		chatGroupService.register(ownerId, ownerNickname, chatGroupRegisterDTO);
		return ResponseVO.success();
	}
}
