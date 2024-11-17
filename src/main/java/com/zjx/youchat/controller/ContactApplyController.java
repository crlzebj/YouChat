package com.zjx.youchat.controller;

import com.zjx.youchat.service.ContactApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contactApplys")
public class ContactApplyController {
	@Autowired
	private ContactApplyService contactApplyService;
}
