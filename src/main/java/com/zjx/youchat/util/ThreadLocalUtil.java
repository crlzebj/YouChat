package com.zjx.youchat.util;

public class ThreadLocalUtil {
    private static final ThreadLocal<String> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> EMAIL = new ThreadLocal<>();

    public static void setUserId(String userId) {
        ThreadLocalUtil.USER_ID.set(userId);
    }

    public static void setEmail(String email) {
        ThreadLocalUtil.EMAIL.set(email);
    }

    public static String getUserId() {
        return ThreadLocalUtil.USER_ID.get();
    }

    public static String getEmail() {
        return ThreadLocalUtil.EMAIL.get();
    }

    public static void remove() {
        ThreadLocalUtil.USER_ID.remove();
        ThreadLocalUtil.EMAIL.remove();
    }
}
