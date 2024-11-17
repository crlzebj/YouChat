package com.zjx.youchat.controller;

import com.zjx.youchat.service.UserContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userContacts")
public class UserContactController {
	@Autowired
	private UserContactService userContactService;
}
