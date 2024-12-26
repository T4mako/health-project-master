package com.keylab.healthproject.controller;

import com.keylab.healthproject.common.Result;
import com.keylab.healthproject.common.ResultCodeEnum;
import com.keylab.healthproject.dao.User;
import com.keylab.healthproject.service.IUserService;
import com.keylab.healthproject.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author T4mako
 * @date 2024/12/25 23:40
 */
@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    IUserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        if (user.getUsername() != null && user.getPassword() != null) {
            String token = userService.login(user.getUsername(), user.getPassword());

            // 存储 Token 到 ThreadLocalUtil
            ThreadLocalUtil.setCurrentUser(String.valueOf(user.getId()));
            Map<String, Object> res = new HashMap<>();
            res.put("token", token);
            return Result.success(res);
        }
        return Result.error(ResultCodeEnum.PARAM_ERROR);
    }

    @PostMapping("/logout")
    public Result logout() {
        String currentUserId = ThreadLocalUtil.getCurrentUser();
        if (currentUserId == null || currentUserId.isEmpty()) {
            return Result.error(ResultCodeEnum.PARAM_ERROR); // 未登录
        }
        ThreadLocalUtil.clear(); // 清除所有变量
        return Result.success("登出成功");
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if(user.getUsername() != null && user.getPassword() != null) {
            userService.register(user.getUsername(), user.getPassword());
            Map<String, Object> res = new HashMap<>();
            res.put("message", "User registered successfully");
            return Result.success(res);
        }else {
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        }

    }
}
