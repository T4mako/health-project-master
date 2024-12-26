package com.keylab.healthproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keylab.healthproject.dao.User;

import java.util.Map;

/**
 * @author T4mako
 * @date 2024/12/25 23:24
 */
public interface IUserService extends IService<User> {

    String login(String username, String password);

    void register(String username, String password);
}
