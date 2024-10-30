package com.keylab.healthproject.service;

import com.keylab.healthproject.dao.HealthData;
import com.keylab.healthproject.dao.PersonData;

public interface IUserService {
    // 获取用户信息，根据id
    PersonData getUserInfo(Long id);

    //
    HealthData getUserHealthInfo(Long id);
}
