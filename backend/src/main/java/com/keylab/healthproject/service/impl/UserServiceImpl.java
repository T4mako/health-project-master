package com.keylab.healthproject.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keylab.healthproject.dao.User;
import com.keylab.healthproject.mapper.UserMapper;
import com.keylab.healthproject.service.IUserService;
import com.keylab.healthproject.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author T4mako
 * @date 2024/12/25 23:25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    private static final String SECRET = "health";

    @Override
    public String login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).eq("password", SecureUtil.md5(password));
        User user = userMapper.selectOne(queryWrapper);

        if (user != null) {
            return JwtUtils.createToken(String.valueOf(user.getId()), user.getUsername());
        }
        return "登录失败";
    }

    @Override
    public void register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(SecureUtil.md5(password));
        userMapper.insert(user);
    }

}
