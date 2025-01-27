package com.zjx.youchat.ws.service;

import com.zjx.youchat.ws.constant.UserConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class URIService {
    private final StringRedisTemplate redisTemplate;

    public void addUser(String userId, String uri) {
        redisTemplate.opsForHash().put(UserConstant.USER_REGISTER, userId, uri);
    }

    public void deleteUser(String userId) {
        redisTemplate.opsForHash().delete(UserConstant.USER_REGISTER, userId);
    }

    public String queryUser(String userId) {
        return (String) redisTemplate.opsForHash().get(UserConstant.USER_REGISTER, userId);
    }

    public void addGroup(String groupId, String uri) {
        String countStr = (String)redisTemplate.opsForHash().get(UserConstant.GROUP_REGISTER + groupId, uri);
        int count = countStr == null ? 0 : Integer.parseInt(countStr);
        redisTemplate.opsForHash().put(UserConstant.GROUP_REGISTER + groupId, uri, String.valueOf(count + 1));
    }

    public void deleteGroup(String groupId, String uri) {
        String countStr = (String)redisTemplate.opsForHash().get(UserConstant.GROUP_REGISTER + groupId, uri);
        if (countStr == null) {
            return;
        }
        int count = Integer.parseInt(countStr);
        if (count == 1) {
            redisTemplate.opsForHash().delete(UserConstant.GROUP_REGISTER + groupId, uri);
        } else {
            redisTemplate.opsForHash().put(UserConstant.GROUP_REGISTER + groupId, uri, String.valueOf(count - 1));
        }
    }

    public List<String> queryGroup(String groupId) {
        List<String> uris = new ArrayList<>();
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(UserConstant.GROUP_REGISTER + groupId);
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            uris.add((String) entry.getKey());
        }
        return uris;
    }
}
