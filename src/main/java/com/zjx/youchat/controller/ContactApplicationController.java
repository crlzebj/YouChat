package com.zjx.youchat.controller;

import com.zjx.youchat.service.ContactApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contactApplications")
public class ContactApplicationController {
	@Autowired
	private ContactApplicationService contactApplicationService;
}
