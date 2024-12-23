package com.zjx.youchat.controller;

import com.zjx.youchat.pojo.dto.ContactAddDTO;
import com.zjx.youchat.pojo.po.Contact;
import com.zjx.youchat.pojo.vo.ContactSearchVO;
import com.zjx.youchat.pojo.vo.ResponseVO;
import com.zjx.youchat.service.ContactService;
import com.zjx.youchat.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
public class ContactController {
	@Autowired
	private ContactService contactService;

	@GetMapping("/search")
	public ResponseVO<ContactSearchVO> search(@RequestParam String contactId) {
		return ResponseVO.success(contactService.search(contactId));
	}

	@PostMapping("/add")
	public ResponseVO add(@RequestBody ContactAddDTO contactaddDTO) {
		contactService.add(contactaddDTO);
		return ResponseVO.success();
	}
}
