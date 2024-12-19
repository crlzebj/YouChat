package com.zjx.youchat.controller;

import com.zjx.youchat.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userContacts")
public class ContactController {
	@Autowired
	private ContactService contactService;
}
