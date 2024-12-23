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

    @Override
    public void upsertHealthData(List<Map<String, Object>> records) {
        for (Map<String, Object> record : records) {
            String createTime = ((String) record.get("create_time")).split(" ")[0];
            String familyUserId = (String) record.get("family_user_id");
            String researchedPersonId = (String) record.get("researched_person_id");

            // 创建字段检查 Map
            Map<String, Object> fieldsToCheck = new HashMap<>();
            fieldsToCheck.put("temperature", record.get("temperature"));
            fieldsToCheck.put("heart_rate", record.get("heart_rate"));
            fieldsToCheck.put("breath_rate", record.get("breath_rate"));
            fieldsToCheck.put("blood_oxygen", record.get("blood_oxygen"));
            fieldsToCheck.put("blood_glucose", record.get("blood_glucose"));
            fieldsToCheck.put("systolic", record.get("systolic"));
            fieldsToCheck.put("diastolic", record.get("diastolic"));
            fieldsToCheck.values().removeIf(Objects::isNull);

            // 使用 MyBatis-Plus 查询记录是否存在
            LambdaQueryWrapper<HealthData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(HealthData::getCreateTime, createTime)
                    .eq(HealthData::getFamilyUserId, familyUserId)
                    .eq(HealthData::getResearchedPersonId, researchedPersonId);

            HealthData existingData = healthDataMapper.selectOne(queryWrapper);

            // 字段映射表
            Map<String, String> fieldMappings = Map.of(
                    "heart_rate", "heartRate",
                    "blood_glucose", "bloodGlucose",
                    "breath_rate", "breathRate",
                    "create_time", "createTime",
                    "blood_oxygen", "bloodOxygen",
                    "family_user_id", "familyUserId",
                    "researched_person_id", "researchedPersonId"
                    // 添加其他字段映射
            );

            if (existingData != null) {
                // 如果存在，则更新
                HealthData updateData = new HealthData();
                updateData.setId(existingData.getId());

                // 动态设置字段值时使用映射表
                for (Map.Entry<String, Object> entry : fieldsToCheck.entrySet()) {
                    String realFieldName = fieldMappings.getOrDefault(entry.getKey(), entry.getKey());
                    try {
                        BeanUtil.setFieldValue(updateData, realFieldName, entry.getValue());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error setting field: " + realFieldName + " - " + e.getMessage());
                    }
                }
                healthDataMapper.updateById(updateData);
            } else {
                // 如果不存在，则插入
                HealthData insertData = new HealthData();
                insertData.setCreateTime(LocalDate.parse(createTime));
                insertData.setFamilyUserId(familyUserId);
                insertData.setResearchedPersonId(researchedPersonId);

                // 动态设置字段值时使用映射表
                for (Map.Entry<String, Object> entry : fieldsToCheck.entrySet()) {
                    String realFieldName = fieldMappings.getOrDefault(entry.getKey(), entry.getKey());
                    try {
                        BeanUtil.setFieldValue(insertData, realFieldName, entry.getValue());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error setting field: " + realFieldName + " - " + e.getMessage());
                    }
                }
                healthDataMapper.insert(insertData);
            }
        }
    }

    @Override
    public HealthData personLatestHData(long id) {
        HealthData res = new HealthData();
        res.setResearchedPersonId(String.valueOf(id));
        // 查询最新的 diastolic
        LambdaQueryWrapper<HealthData> latestDiastolic = new LambdaQueryWrapper<>();
        latestDiastolic.eq(HealthData::getResearchedPersonId, id)
                .isNotNull(HealthData::getDiastolic)
                .orderByDesc(HealthData::getCreateTime)
                .last("limit 1");
        HealthData diastolicData = healthDataMapper.selectOne(latestDiastolic);
        if (diastolicData != null) {
            res.setDiastolic(diastolicData.getDiastolic());
        }

        // 查询最新的 systolic
        LambdaQueryWrapper<HealthData> latestSystolic = new LambdaQueryWrapper<>();
        latestSystolic.eq(HealthData::getResearchedPersonId, id)
                .isNotNull(HealthData::getSystolic)
                .orderByDesc(HealthData::getCreateTime)
                .last("limit 1");
        HealthData systolicData = healthDataMapper.selectOne(latestSystolic);
        if (systolicData != null) {
            res.setSystolic(systolicData.getSystolic());
        }

        // 查询最新的 breath_rate
        LambdaQueryWrapper<HealthData> latestBreathRate = new LambdaQueryWrapper<>();
        latestBreathRate.eq(HealthData::getResearchedPersonId, id)
                .isNotNull(HealthData::getBreathRate)
                .orderByDesc(HealthData::getCreateTime)
                .last("limit 1");
        HealthData breathRateData = healthDataMapper.selectOne(latestBreathRate);
        if (breathRateData != null) {
            res.setBreathRate(breathRateData.getBreathRate());
        }

        // 查询最新的 blood_oxygen
        LambdaQueryWrapper<HealthData> latestBloodOxygen = new LambdaQueryWrapper<>();
        latestBloodOxygen.eq(HealthData::getResearchedPersonId, id)
                .isNotNull(HealthData::getBloodOxygen)
                .orderByDesc(HealthData::getCreateTime)
                .last("limit 1");
        HealthData bloodOxygenData = healthDataMapper.selectOne(latestBloodOxygen);
        if (bloodOxygenData != null) {
            res.setBloodOxygen(bloodOxygenData.getBloodOxygen());
        }

        // 查询最新的 temperature
        LambdaQueryWrapper<HealthData> latestTemperature = new LambdaQueryWrapper<>();
        latestTemperature.eq(HealthData::getResearchedPersonId, id)
                .isNotNull(HealthData::getTemperature)
                .orderByDesc(HealthData::getCreateTime)
                .last("limit 1");
        HealthData temperatureData = healthDataMapper.selectOne(latestTemperature);
        if (temperatureData != null) {
            res.setTemperature(temperatureData.getTemperature());
        }

        // 查询最新的 heart_rate
        LambdaQueryWrapper<HealthData> latestHeartRate = new LambdaQueryWrapper<>();
        latestHeartRate.eq(HealthData::getResearchedPersonId, id)
                .isNotNull(HealthData::getHeartRate)
                .orderByDesc(HealthData::getCreateTime)
                .last("limit 1");
        HealthData heartRateData = healthDataMapper.selectOne(latestHeartRate);
        if (heartRateData != null) {
            res.setHeartRate(heartRateData.getHeartRate());
        }

        // 查询最新的 blood_glucose
        LambdaQueryWrapper<HealthData> latestBloodGlucose = new LambdaQueryWrapper<>();
        latestBloodGlucose.eq(HealthData::getResearchedPersonId, id)
                .isNotNull(HealthData::getBloodGlucose)
                .orderByDesc(HealthData::getCreateTime)
                .last("limit 1");
        HealthData bloodGlucoseData = healthDataMapper.selectOne(latestBloodGlucose);
        if (bloodGlucoseData != null) {
            res.setBloodGlucose(bloodGlucoseData.getBloodGlucose());
        }

        return res;
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
