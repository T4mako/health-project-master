package com.keylab.healthproject.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.keylab.healthproject.dao.HealthData;
import com.keylab.healthproject.dao.PersonData;
import com.keylab.healthproject.mapper.HealthDataMapper;
import com.keylab.healthproject.mapper.PersonDataMapper;
import com.keylab.healthproject.service.IHealthDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    Date today = DateUtil.parse("2024-10-31");

    @Autowired
    HealthDataMapper healthDataMapper;

    @Autowired
    PersonDataMapper personDataMapper;

    @Override
    public List<HealthData> dayHData(long id) {
        LambdaQueryWrapper<HealthData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HealthData::getResearchedPersonId,id).eq(HealthData::getCreateTime,today);
        return healthDataMapper.selectList(queryWrapper);
    }

    @Override
    public List<HealthData> dateHData(long id, String date) {
        LambdaQueryWrapper<HealthData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HealthData::getResearchedPersonId, id);

        // 根据date的值选择不同的查询条件
        switch (date) {
            case "week":
                queryWrapper.ge(HealthData::getCreateTime,DateUtil.offsetWeek(today,-1));
                break;
            case "month":
                queryWrapper.ge(HealthData::getCreateTime, DateUtil.offsetMonth(today, -1)); // 查询过去一个月的数据
                break;
            case "year":
                queryWrapper.ge(HealthData::getCreateTime, DateUtil.offsetMonth(today, -12)); // 查询过去一年的数据
                break;
            case "all":
                // 不添加时间限制，查询全部数据
                break;
        }

        return healthDataMapper.selectList(queryWrapper);
    }

    @Override
    public List<Map<String, Object>> getDataByArea(long id,int flag, String indicator) {
        List<Map<String, Object>> res = null;
        if(flag==0){
            // 查询区数据
            res = healthDataMapper.selectAllCommunityCompareData(id,indicator);
        }else if(flag==1){
            // 查询市数据
            res = healthDataMapper.selectAllProvinceCompareData(id,indicator);
        }else{
            // 查询全部数据
            res = healthDataMapper.selectAllCompareData(indicator);
        }
        return res;
    }
}
