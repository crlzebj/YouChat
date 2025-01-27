package com.zjx.youchat.api.client;

import com.zjx.youchat.api.dto.PersonalInfoDTO;
import com.zjx.youchat.api.dto.GroupContact;
import com.zjx.youchat.api.dto.Message;
import com.zjx.youchat.api.dto.Session;
import com.zjx.youchat.api.dto.UserContact;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient("chat-service")
public interface ChatClient {
    @GetMapping("/user/me")
    PersonalInfoDTO getPersonalInfo(@RequestHeader("user-id") String userId,
                                    @RequestHeader("email") String email);

    @GetMapping("/contact/myFriend")
    List<UserContact> getMyFriend(@RequestHeader("user-id") String userId,
                                  @RequestHeader("email") String email);

    @GetMapping("/contact/myGroup")
    List<GroupContact> getMyGroup(@RequestHeader("user-id") String userId,
                                  @RequestHeader("email") String email);

    @GetMapping("/session/mySession")
    List<Session> getMySession(@RequestHeader("user-id") String userId,
                               @RequestHeader("email") String email);

    @GetMapping("/message/myMessage")
    List<Message> getMyMessage(@RequestHeader("user-id") String userId,
                               @RequestHeader("email") String email);
}
