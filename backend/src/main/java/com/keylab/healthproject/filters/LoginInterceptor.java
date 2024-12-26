package com.keylab.healthproject.filters;

import cn.hutool.jwt.JWT;
import com.keylab.healthproject.util.JwtUtils;
import com.keylab.healthproject.util.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;

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
            // 设置响应状态码
            response.setStatus(HttpServletResponse.SC_OK);
            // 设置响应内容类型为 JSON
            response.setContentType("application/json;charset=UTF-8");
            // 将 Result 对象转换为 JSON 字符串
            String jsonResult = "{\"code\":401,\"message\":\"未授权，请先登录\",\"data\":null}";
            // 写入响应
            PrintWriter writer = response.getWriter();
            writer.write(jsonResult);
            writer.flush();
            writer.close();
            return false;
        }
    }

}
