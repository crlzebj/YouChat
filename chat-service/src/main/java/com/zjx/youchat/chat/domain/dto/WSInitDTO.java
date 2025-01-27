package com.zjx.youchat.chat.domain.dto;

import com.zjx.youchat.chat.domain.po.GroupContact;
import com.zjx.youchat.chat.domain.po.Message;
import com.zjx.youchat.chat.domain.po.Session;
import com.zjx.youchat.chat.domain.po.UserContact;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WSInitDTO implements Serializable {
    private PersonalInfoDTO personalInfo;
    private List<UserContact> userContacts;
    private List<GroupContact> groupContacts;
    private List<Session> sessions;
    private List<Message> messages;
}
