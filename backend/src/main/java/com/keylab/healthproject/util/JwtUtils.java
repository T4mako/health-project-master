package com.keylab.healthproject.util;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSignerUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author T4mako
 * @date 2024/12/26 13:52
 */
public class JwtUtils {

    private static final String SECRET_KEY = "health"; // 替换为你的密钥

    // 生成JWT令牌
    public static String createToken(String userId, String username) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("username", username);
        payload.put("exp", System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7); // 设置过期时间为7天

        return JWTUtil.createToken(payload, JWTSignerUtil.hs256(SECRET_KEY.getBytes()));
    }

    // 验证JWT令牌
    public static boolean verifyToken(String token) {
        return JWTUtil.verify(token, JWTSignerUtil.hs256(SECRET_KEY.getBytes()));
    }

    // 解析JWT令牌
    public static JWT parseToken(String token) {
        return JWTUtil.parseToken(token);
    }
}
