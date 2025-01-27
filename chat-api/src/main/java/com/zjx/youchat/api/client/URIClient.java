package com.zjx.youchat.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("ws-service")
public interface URIClient {
    @GetMapping("/uri/user")
    String queryUser(@RequestParam String userId);

    @GetMapping("/uri/group")
    List<String> queryGroup(@RequestParam String groupId);
}
