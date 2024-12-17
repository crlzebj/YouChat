package com.zjx.youchat.controller;

import com.zjx.youchat.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatSessions")
public class SessionController {
	@Autowired
	private SessionService sessionService;
}
