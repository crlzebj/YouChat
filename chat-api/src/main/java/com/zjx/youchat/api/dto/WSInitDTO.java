package com.zjx.youchat.api.dto;


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
