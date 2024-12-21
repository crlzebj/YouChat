package com.zjx.youchat.pojo.dto;

import com.zjx.youchat.pojo.po.Contact;
import com.zjx.youchat.pojo.po.Message;
import com.zjx.youchat.pojo.po.Session;
import lombok.Data;

import java.util.List;

@Data
public class UserInfoDTO {
    public List<Contact> userContacts;
    public List<Contact> chatGroupContacts;
    public List<Session> sessions;
    public List<Message> messages;
}
