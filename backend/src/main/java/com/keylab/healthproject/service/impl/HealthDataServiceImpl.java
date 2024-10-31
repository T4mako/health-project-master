package com.keylab.healthproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.keylab.healthproject.dao.HealthData;
import com.keylab.healthproject.mapper.HealthDataMapper;
import com.keylab.healthproject.service.IHealthDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author T4mako
 * @since 2024-10-24
 */
@Service
public class HealthDataServiceImpl extends ServiceImpl<HealthDataMapper, HealthData> implements IHealthDataService {

    @Autowired
    HealthDataMapper healthDataMapper;

    @Override
    public List<HealthData> dayHData(long id) {
        String date = "2024-10-31";
        LambdaQueryWrapper<HealthData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HealthData::getResearchedPersonId,id).eq(HealthData::getCreateTime,date);
        return healthDataMapper.selectList(queryWrapper);
    }
}
