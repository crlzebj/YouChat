package com.zjx.youchat.util;

public class ThreadLocalUtil {
    private static final ThreadLocal<String> id = new ThreadLocal<>();
    private static final ThreadLocal<String> email = new ThreadLocal<>();
    private static final ThreadLocal<String> nickname = new ThreadLocal<>();

    public static void setId(String id) {
        ThreadLocalUtil.id.set(id);
    }

    public static void setEmail(String email) {
        ThreadLocalUtil.email.set(email);
    }

    public static void setNickname(String nickname) {
        ThreadLocalUtil.nickname.set(nickname);
    }

    public static String getId() {
        return ThreadLocalUtil.id.get();
    }

    public static String getEmail() {
        return ThreadLocalUtil.email.get();
    }

    public static String getNickname() {
        return ThreadLocalUtil.nickname.get();
    }

    public static void remove() {
        ThreadLocalUtil.id.remove();
        ThreadLocalUtil.email.remove();
        ThreadLocalUtil.nickname.remove();
    }
}
