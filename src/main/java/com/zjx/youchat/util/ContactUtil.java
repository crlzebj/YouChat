package com.zjx.youchat.util;

import com.zjx.youchat.pojo.po.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactUtil {
    public static List<List<Contact>> splitContact(String userId,
                                                   List<Contact> contacts) {
        List<List<Contact>> list = new ArrayList<>();
        list.add(new ArrayList<>());
        list.add(new ArrayList<>());
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 0);
        map.put(1, 2);
        map.put(2, 1);
        map.put(3, 4);
        map.put(4, 3);
        map.put(5, 5);
        for (Contact contact : contacts) {
            if (!contact.getInitiatorId().equals(userId)) {
                contact.setAccepterId(contact.getInitiatorId());
                contact.setInitiatorId(userId);
                contact.setStatus(map.get(contact.getStatus()));
            }
            if (contact.getAccepterId().startsWith("U")) {
                list.get(0).add(contact);
            } else {
                list.get(1).add(contact);
            }
        }
        return list;
    }
}
