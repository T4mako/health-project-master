package com.keylab.healthproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.keylab.healthproject.dao.HealthData;
import com.keylab.healthproject.dao.PersonData;
import com.keylab.healthproject.mapper.HealthDataMapper;
import com.keylab.healthproject.mapper.PersonDataMapper;
import com.keylab.healthproject.service.IHealthDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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

    @Autowired
    PersonDataMapper personDataMapper;

    @Override
    public List<HealthData> dayHData(long id) {
        HealthData result = new HealthData();
//        return healthDataMapper.getLatestHData(id);
        // 查询呼吸率（breath_rate）的最近值
        HealthData breathRateData = getLatestData(id, HealthData::getBreathRate);
        result.setBreathRate(breathRateData != null ? breathRateData.getBreathRate() : 0);

        // 查询收缩压（systolic）的最近值
        HealthData systolicData = getLatestData(id, HealthData::getSystolic);
        result.setSystolic(systolicData != null ? systolicData.getSystolic() : 0);

        // 查询舒张压（diastolic）的最近值
        HealthData diastolicData = getLatestData(id, HealthData::getDiastolic);
        result.setDiastolic(diastolicData != null ? diastolicData.getDiastolic() : 0);

        // 查询血氧（blood_oxygen）的最近值
        HealthData bloodOxygenData = getLatestData(id, HealthData::getBloodOxygen);
        result.setBloodOxygen(bloodOxygenData != null ? bloodOxygenData.getBloodOxygen() : 0);

        // 查询体温（temperature）的最近值
        HealthData temperatureData = getLatestData(id, HealthData::getTemperature);
        result.setTemperature(temperatureData != null ? temperatureData.getTemperature() : 0);

        // 查询心率（heart_rate）的最近值
        HealthData heartRateData = getLatestData(id, HealthData::getHeartRate);
        result.setHeartRate(heartRateData != null ? heartRateData.getHeartRate() : 0);

        // 查询血糖（blood_glucose）的最近值
        HealthData bloodGlucoseData = getLatestData(id, HealthData::getBloodGlucose);
        result.setBloodGlucose(bloodGlucoseData != null ? bloodGlucoseData.getBloodGlucose() : 0);
        List<HealthData> res = new ArrayList<>();
        res.add(result);
        return res;
    }

    private HealthData getLatestData(long id, SFunction<HealthData, ?> field) {
        LambdaQueryWrapper<HealthData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HealthData::getResearchedPersonId, id)
                .orderByDesc(HealthData::getCreateTime)
                .isNotNull(field)
                .last("LIMIT 1");

        List<HealthData> resultList = healthDataMapper.selectList(queryWrapper);
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    @Override
    public List<HealthData> dateHData(long id, String date) {
        LambdaQueryWrapper<HealthData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HealthData::getResearchedPersonId, id);

        LocalDate today = LocalDate.now();
        LocalDate startDate = today;

        // 根据date的值选择不同的查询条件
        switch (date) {
            case "week":
                startDate = today.minusWeeks(1);
                break;
            case "month":
                startDate = today.minusMonths(1);
                break;
            case "year":
                startDate = today.minusYears(1);
                break;
            case "all":
                // 不添加时间限制，查询全部数据
                break;
        }

        if (!date.equals("all")) {
            queryWrapper.ge(HealthData::getCreateTime, startDate);
        }

        List<HealthData> queryResult = healthDataMapper.selectList(queryWrapper);

        // 生成包含所有日期的列表
        List<HealthData> resultList = new ArrayList<>();
        for (LocalDate currentDate = startDate; currentDate.isBefore(today.plusDays(1)); currentDate = currentDate.plusDays(1)) {
            LocalDate finalCurrentDate = currentDate;
            HealthData data = queryResult.stream()
                    .filter(d -> d.getCreateTime().equals(finalCurrentDate))
                    .findFirst()
                    .orElse(createEmptyHealthData(currentDate));
            resultList.add(data);
        }

        return resultList;
    }

    private HealthData createEmptyHealthData(LocalDate date) {
        HealthData emptyData = new HealthData();
        emptyData.setCreateTime(date);
        emptyData.setBreathRate(0);
        emptyData.setSystolic(0);
        emptyData.setDiastolic(0);
        emptyData.setBloodOxygen(0);
        emptyData.setTemperature(0);
        emptyData.setHeartRate(0);
        emptyData.setBloodGlucose(0);
        return emptyData;
    }

    @Override
    public List<Map<String, Object>> getAreaHDataAge(long id,String area, String indicator) {
        List<Map<String, Object>> res = null;
        if(area.equals("community")){
            // 查询区数据
                res = healthDataMapper.selectAllCommunityCompareData(id,indicator);
        }else if(area.equals("city")){
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
        HealthData result = new HealthData();
        result.setId(id);
        // 查询呼吸率（breath_rate）的最近值
        HealthData breathRateData = getLatestData(id, HealthData::getBreathRate);
        result.setBreathRate(breathRateData != null ? breathRateData.getBreathRate() : 0);

        // 查询收缩压（systolic）的最近值
        HealthData systolicData = getLatestData(id, HealthData::getSystolic);
        result.setSystolic(systolicData != null ? systolicData.getSystolic() : 0);

        // 查询舒张压（diastolic）的最近值
        HealthData diastolicData = getLatestData(id, HealthData::getDiastolic);
        result.setDiastolic(diastolicData != null ? diastolicData.getDiastolic() : 0);

        // 查询血氧（blood_oxygen）的最近值
        HealthData bloodOxygenData = getLatestData(id, HealthData::getBloodOxygen);
        result.setBloodOxygen(bloodOxygenData != null ? bloodOxygenData.getBloodOxygen() : 0);

        // 查询体温（temperature）的最近值
        HealthData temperatureData = getLatestData(id, HealthData::getTemperature);
        result.setTemperature(temperatureData != null ? temperatureData.getTemperature() : 0);

        // 查询心率（heart_rate）的最近值
        HealthData heartRateData = getLatestData(id, HealthData::getHeartRate);
        result.setHeartRate(heartRateData != null ? heartRateData.getHeartRate() : 0);

        // 查询血糖（blood_glucose）的最近值
        HealthData bloodGlucoseData = getLatestData(id, HealthData::getBloodGlucose);
        result.setBloodGlucose(bloodGlucoseData != null ? bloodGlucoseData.getBloodGlucose() : 0);

        List<String> warnings = new ArrayList<>();

        warnings.add("breath_rate: " + evaluateBreathRate(result.getBreathRate()));
        warnings.add("systolic: " + evaluateSystolic(result.getSystolic()));
        warnings.add("diastolic: " + evaluateDiastolic(result.getDiastolic()));
        warnings.add("blood_oxygen: " + evaluateBloodOxygen(result.getBloodOxygen()));
        warnings.add("temperature: " + evaluateTemperature(result.getTemperature()));
        warnings.add("heart_rate: " + evaluateHeartRate(result.getHeartRate()));
        warnings.add("blood_glucose: " + evaluateBloodGlucose(result.getBloodGlucose()));

        return warnings;
    }

    @Override
    public List<Map<String, Object>> getAgeIndicator(long id,String indicator) {
        return healthDataMapper.getAgeIndicator(id,indicator);
    }

    @Override
    public List<Map<String, Object>> communityAllInfo(long id) {
        return healthDataMapper.communityAllInfo(id);
    }

    @Override
    public List<Map<String, Object>> getLatestFullHData(long id) {
        return healthDataMapper.getLatestFullHData(id);
    }

    private String evaluateBreathRate(Long breathRate) {
        if (breathRate == 0) return "数据缺失";
        if (breathRate <= 5) return "安全";
        if (breathRate <= 20) return "一级预警";
        return "二级预警";
    }

    private String evaluateSystolic(Long systolic) {
        if (systolic == 0) return "数据缺失";
        if (systolic >= 90 && systolic <= 139) return "安全";
        if ((systolic >= 140 && systolic <= 159))  return "一级预警";
        if ((systolic >= 160 && systolic <= 179) || (systolic >= 80 && systolic <= 89))  return "二级预警";
        else return "三级预警";
    }

    private String evaluateDiastolic(Long diastolic) {
        if (diastolic == 0) return "数据缺失";
        if (diastolic >= 60 && diastolic <= 89) return "安全";
        if (diastolic >= 90 && diastolic <= 99) return "一级预警";
        if (diastolic >= 100 && diastolic <= 109) return "二级预警";
        else return "三级预警";
    }

    private String evaluateBloodOxygen(Long bloodOxygen) {
        if (bloodOxygen == 0) return "数据缺失";
        if (bloodOxygen >= 95) return "安全";
        if (bloodOxygen >= 90) return "一级预警";
        if (bloodOxygen >= 85) return "二级预警";
        return "三级预警";
    }

    private String evaluateTemperature(Double temperature) {
        if (temperature == 0) return "数据缺失";
        if (temperature >= 36.0 && temperature <= 37.3) return "安全";
        if (temperature >= 37.4 && temperature <= 39.0) return "一级预警";
        if (temperature >= 39.1) return "二级预警";
        return "";
    }

    private String evaluateHeartRate(Long heartRate) {
        if (heartRate == 0) return "数据缺失";
        if (heartRate >= 60 && heartRate <= 100) return "安全";
        if ((heartRate >= 110 && heartRate <= 130) || (heartRate >= 50 && heartRate <= 59)) return "一级预警";
        if ((heartRate >= 140 && heartRate <= 180) || (heartRate >= 40 && heartRate <= 49)) return "二级预警";
        if (heartRate > 180 || heartRate < 40) return "三级预警";
        return "";
    }

    private String evaluateBloodGlucose(Double bloodGlucose) {
        if (bloodGlucose == 0) return "数据缺失";
        if (bloodGlucose >= 4 && bloodGlucose <= 11) return "安全";
        if ((bloodGlucose > 11 && bloodGlucose <= 19.9) || (bloodGlucose >= 3.5 && bloodGlucose <= 3.9)) return "一级预警";
        if ((bloodGlucose >= 20 && bloodGlucose <= 29.9) || (bloodGlucose >= 3 && bloodGlucose < 3.5)) return "二级预警";
        else return "三级预警";
    }
}
