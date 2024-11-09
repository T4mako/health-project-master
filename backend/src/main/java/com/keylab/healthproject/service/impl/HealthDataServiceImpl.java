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

    @Override
    public List<String> getThresholdWarnings(long id) {
        HealthData healthData = healthDataMapper.findByResearchedPersonId(id);
        List<String> warnings = new ArrayList<>();

        if (healthData != null) {
            warnings.add("breath_rate: " + evaluateBreathRate(healthData.getBreathRate()));
            warnings.add("systolic: " + evaluateSystolic(healthData.getSystolic()));
            warnings.add("diastolic: " + evaluateDiastolic(healthData.getDiastolic()));
            warnings.add("blood_oxygen: " + evaluateBloodOxygen(healthData.getBloodOxygen()));
            warnings.add("temperature: " + evaluateTemperature(healthData.getTemperature()));
            warnings.add("heart_rate: " + evaluateHeartRate(healthData.getHeartRate()));
            warnings.add("blood_glucose: " + evaluateBloodGlucose(healthData.getBloodGlucose()));
        }

        return warnings;
    }

    private String evaluateBreathRate(Long breathRate) {
        if (breathRate == null) return "数据缺失";
        if (breathRate <= 5) return "安全";
        if (breathRate <= 20) return "一级预警";
        return "二级预警";
    }

    private String evaluateSystolic(Long systolic) {
        if (systolic == null) return "数据缺失";
        if (systolic >= 90 && systolic <= 139) return "安全";
        if ((systolic >= 149 && systolic <= 159) || (systolic >= 80 && systolic <= 89)) return "一级预警";
        if (systolic >= 160 && systolic <= 179) return "二级预警";
        if (systolic >= 180 || systolic <= 79) return "三级预警";
        return "";
    }

    private String evaluateDiastolic(Long diastolic) {
        if (diastolic == null) return "数据缺失";
        if (diastolic >= 60 && diastolic <= 89) return "安全";
        if (diastolic >= 90 && diastolic <= 99) return "一级预警";
        if (diastolic >= 100 && diastolic <= 109) return "二级预警";
        if (diastolic >= 110) return "三级预警";
        return "";
    }

    private String evaluateBloodOxygen(Long bloodOxygen) {
        if (bloodOxygen == null) return "数据缺失";
        if (bloodOxygen >= 95) return "安全";
        if (bloodOxygen >= 90 && bloodOxygen <= 94) return "一级预警";
        if (bloodOxygen >= 85 && bloodOxygen <= 89) return "二级预警";
        if (bloodOxygen <= 84) return "三级预警";
        return "";
    }

    private String evaluateTemperature(Double temperature) {
        if (temperature == null) return "数据缺失";
        if (temperature >= 36.0 && temperature <= 37.3) return "安全";
        if (temperature >= 37.4 && temperature <= 39.0) return "一级预警";
        if (temperature >= 39.1) return "二级预警";
        return "";
    }

    private String evaluateHeartRate(Long heartRate) {
        if (heartRate == null) return "数据缺失";
        if (heartRate >= 60 && heartRate <= 100) return "安全";
        if ((heartRate >= 110 && heartRate <= 130) || (heartRate >= 50 && heartRate <= 59)) return "一级预警";
        if ((heartRate >= 140 && heartRate <= 180) || (heartRate >= 40 && heartRate <= 49)) return "二级预警";
        if (heartRate > 180 || heartRate < 40) return "三级预警";
        return "";
    }

    private String evaluateBloodGlucose(Double bloodGlucose) {
        if (bloodGlucose == null) return "数据缺失";
        if (bloodGlucose >= 3.9 && bloodGlucose <= 6.1) return "安全";
        if ((bloodGlucose >= 11.1 && bloodGlucose <= 19.9) || (bloodGlucose >= 3.0 && bloodGlucose < 3.9)) return "一级预警";
        if ((bloodGlucose >= 20 && bloodGlucose <= 29.9) || (bloodGlucose >= 2.0 && bloodGlucose < 3.0)) return "二级预警";
        if (bloodGlucose >= 30 || bloodGlucose < 2) return "三级预警";
        return "";
    }
}
