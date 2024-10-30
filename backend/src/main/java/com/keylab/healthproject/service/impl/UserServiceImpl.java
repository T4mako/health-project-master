package com.keylab.healthproject.service.impl;

import com.keylab.healthproject.dao.PersonData;
import com.keylab.healthproject.mapper.UserMapper;
import com.keylab.healthproject.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public PersonData getUserInfo(Long id) {
        return userMapper.getUserInfo(id);
    }
}
