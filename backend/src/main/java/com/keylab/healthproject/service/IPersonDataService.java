package com.keylab.healthproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keylab.healthproject.dao.HealthData;
import com.keylab.healthproject.dao.PersonData;

import java.util.List;

/**
 * @author T4mako
 * @date 2024/10/29 16:02
 */
public interface IPersonDataService extends IService<PersonData> {
    List<HealthData> getDayHData(long id);
}
