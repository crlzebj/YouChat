package com.zjx.youchat.ws.controller;

import com.zjx.youchat.ws.service.URIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/uri")
public class URIController {
    private final URIService URIService;

    @GetMapping("/user")
    public String queryUser(@RequestParam String userId) {
        return URIService.queryUser(userId);
    }

    @GetMapping("/group")
    public List<String> queryGroup(@RequestParam String groupId) {
        return URIService.queryGroup(groupId);
    }
}
