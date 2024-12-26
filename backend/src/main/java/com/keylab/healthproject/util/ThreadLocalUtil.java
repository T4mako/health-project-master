package com.keylab.healthproject.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author T4mako
 * @date 2024/12/26 13:34
 */
public class ThreadLocalUtil {
    // 使用 ThreadLocal 存储线程级别的变量
    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(String userId) {
        currentUser.set(userId);
    }

    public static String getCurrentUser() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }
}