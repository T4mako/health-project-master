package com.keylab.healthproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keylab.healthproject.dao.HealthData;
import com.keylab.healthproject.dao.PersonData;
import com.keylab.healthproject.mapper.HealthDataMapper;
import com.keylab.healthproject.mapper.PersonDataMapper;
import com.keylab.healthproject.service.IPersonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author T4mako
 * @date 2024/10/29 16:03
 */
@Service
public class PersonDataServiceImpl extends ServiceImpl<PersonDataMapper, PersonData> implements IPersonDataService {

    @Autowired
    PersonDataMapper personDataMapper;

    @Autowired
    HealthDataMapper healthDataMapper;

    @Override
    public List<HealthData> getDayHData(long id) {
        String date = "2024-10-01";
        LambdaQueryWrapper<HealthData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HealthData::getResearchedPersonId,id).eq(HealthData::getCreateTime,date);
        List<HealthData> healthData = healthDataMapper.selectList(queryWrapper);
        return healthData;
    }

    @Override
    public List<Map<String, Object>> allUserBaseInfo() {
        return personDataMapper.allUserBaseInfo();
    }
}
