package com.zjx.youchat.controller;

import com.zjx.youchat.pojo.po.Contact;
import com.zjx.youchat.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
public class ContactController {
	@Autowired
	private ContactService contactService;

	@PostMapping("/search")
	public void search(@RequestBody long contactId) {

	}

	@PostMapping("/add")
	public void add(@RequestBody Contact contact) {

	}
}
