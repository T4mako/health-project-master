package com.keylab.healthproject.filters;

import cn.hutool.jwt.JWT;
import com.keylab.healthproject.util.JwtUtils;
import com.keylab.healthproject.util.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * @author T4mako
 * @date 2024/12/25 23:36
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        String token = request.getHeader("Authorization");
        if (token != null && JwtUtils.verifyToken(token)) {
            JWT jwt = JwtUtils.parseToken(token);
            String userId = (String) jwt.getPayload("userId");
            ThreadLocalUtil.setCurrentUser(userId);
            return true;
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

}
