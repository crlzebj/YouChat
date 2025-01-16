package com.zjx.youchat.chat.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisUtil {
    private final long BEGIN_TIMESTAMP = 1693497600L;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public long generateId(String prefix) {
        LocalDateTime now = LocalDateTime.now();
        long timestamp = now.toEpochSecond(ZoneOffset.UTC) - BEGIN_TIMESTAMP;
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = redisTemplate.opsForValue().increment(prefix + date);
        return timestamp << 32 | count;
    }
}
