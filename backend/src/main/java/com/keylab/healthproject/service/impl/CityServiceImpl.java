package com.keylab.healthproject.service.impl;

import com.keylab.healthproject.mapper.CityMapper;
import com.keylab.healthproject.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CityServiceImpl implements ICityService {
    @Autowired
    private CityMapper cityMapper;

    @Override
    public List<Map<String, Object>> getCityNameAndNum() {
        return cityMapper.getCityNameAndNum();
    }

    @Override
    public long getNumByCityName(String cityName) {
        if (cityName.equals("西安市") || cityName.equals("陕西省")) {
            return cityMapper.getNumByCityId(12);
        } else if (cityName.equals("郑州市") || cityName.equals("河南省")) {
            return cityMapper.getNumByCityId(11);
        } else if (cityName.equals("徐州市") || cityName.equals("江苏省"))
            return cityMapper.getNumByCityId(10);
        else
            return 0;
    }

    @Override
    public List<Map<String, Object>> getSexCount() {
        List<Map<String, Object>> list = cityMapper.getSexCount();
        Map<String, Object> data = list.get(0);
        BigDecimal maleCountBD = (BigDecimal) data.get("男");
        BigDecimal femaleCountBD = (BigDecimal) data.get("女");
        Integer maleCount = maleCountBD.intValue();
        Integer femaleCount = femaleCountBD.intValue();
        // 计算比例
        Double ratio = 0.0;
        if (femaleCount != 0) {
            ratio = maleCount.doubleValue() / femaleCount.doubleValue();
        }
        Map<String, Object> ratioMap = new HashMap<>();
        ratioMap.put("男", maleCount);
        ratioMap.put("女", femaleCount);
        ratioMap.put("ratio", ratio);
        list.remove(0);
        list.add(ratioMap);
        return list;
    }

    @Override
    public List<Map<String, Object>> getSexCountByCity(String cityName) {
        List<Map<String, Object>> list;
        if (cityName.equals("西安市") || cityName.equals("陕西省")) {
            list = cityMapper.getSexCountByCity(12);
        } else if (cityName.equals("郑州市") || cityName.equals("河南省")) {
            list = cityMapper.getSexCountByCity(11);
        } else if (cityName.equals("徐州市") || cityName.equals("江苏省"))
            list = cityMapper.getSexCountByCity(10);
        else
            return null;
        Map<String, Object> data = list.get(0);
        BigDecimal maleCountBD = (BigDecimal) data.get("男");
        BigDecimal femaleCountBD = (BigDecimal) data.get("女");

        Integer maleCount = maleCountBD.intValue();
        Integer femaleCount = femaleCountBD.intValue();
        Double ratio = 0.0;
        if (femaleCount != 0) {
            ratio = maleCount.doubleValue() / femaleCount.doubleValue();
        }
        Map<String, Object> ratioMap = new HashMap<>();
        ratioMap.put("男", maleCount);
        ratioMap.put("女", femaleCount);
        ratioMap.put("ratio", ratio);
        list.remove(0);
        list.add(ratioMap);
        return list;
    }

    @Override
    public List<Map<String, Object>> getHealthStatus() {
        return cityMapper.getHealthStatus();
    }

    @Override
    public List<Map<String, Object>> getHealthStatusByCity(String cityName) {
        List<Map<String, Object>> list;
        if (cityName.equals("西安市") || cityName.equals("陕西省")) {
            list = cityMapper.getHealthStatusByCity(12);
        } else if (cityName.equals("郑州市") || cityName.equals("河南省")) {
            list = cityMapper.getHealthStatusByCity(11);
        } else if (cityName.equals("徐州市") || cityName.equals("江苏省"))
            list = cityMapper.getHealthStatusByCity(10);
        else
            return null;
        return list;
    }

    @Override
    public List<Map<String, Object>> getHealthLevel() {
        return cityMapper.getHealthLevel();
    }

    @Override
    public List<Map<String, Object>> getHealthLevelByCity(String cityName) {
        List<Map<String, Object>> list = new ArrayList();
        if (cityName.equals("西安市") || cityName.equals("陕西省")) {
            list = cityMapper.getHealthLevelByCity(12);
        } else if (cityName.equals("郑州市") || cityName.equals("河南省")) {
            list = cityMapper.getHealthLevelByCity(11);
        } else if (cityName.equals("徐州市") || cityName.equals("江苏省"))
            list = cityMapper.getHealthLevelByCity(10);
        else
            return null;
        return list;
    }

    @Override
    public Map<String, Object> getHealthDataByCommunity(String communityName) {
        return cityMapper.getHealthDataByCommunity(communityName);
    }

    private String calculateBreathRateLevel(Double breathRate) {
        if (breathRate == 0) return "error";
        if (breathRate >= 16 && breathRate <= 20) return "L0";
        if (breathRate >= 11 && breathRate < 16) return "L1";
        if (breathRate >= 8 && breathRate < 11) return "L2";
        return "L3"; // < 8
    }

    private String calculateSystolicLevel(Double systolic) {
        if (systolic == 0) return "error";
        if (systolic >= 90 && systolic < 140) return "L0";
        if (systolic >= 140 && systolic < 160) return "L1";
        if ((systolic >= 160 && systolic < 180) || (systolic >= 80 && systolic < 90)) return "L2";
        return "L3"; // ≥180 或 <80
    }

    private String calculateDiastolicLevel(Double diastolic) {
        if (diastolic == 0) return "error";
        if (diastolic >= 60 && diastolic < 90) return "L0";
        if (diastolic >= 90 && diastolic < 100) return "L1";
        if (diastolic >= 100 && diastolic < 110) return "L2";
        return "L3"; // ≥110
    }

    private String calculateBloodOxygenLevel(Double bloodOxygen) {
        if (bloodOxygen == 0) return "error";
        if (bloodOxygen >= 95 && bloodOxygen <= 100) return "L0";
        if (bloodOxygen >= 90 && bloodOxygen < 95) return "L1";
        if (bloodOxygen >= 85 && bloodOxygen < 90) return "L2";
        return "L3"; // ≤84
    }

    private String calculateTemperatureLevel(Double temperature) {
        if (temperature == 0) return "error";
        if (temperature >= 36 && temperature <= 37.3) return "L0";
        if (temperature >= 37.4 && temperature < 39) return "L1";
        return "L2"; // ≥39.1
    }

    private String calculateHeartRateLevel(Double heartRate) {
        if (heartRate == 0) return "error";
        if (heartRate >= 50 && heartRate <= 120) return "L0";
        if ((heartRate > 120 && heartRate < 160) || (heartRate >= 45 && heartRate < 50)) return "L1";
        if ((heartRate >= 160 && heartRate < 180) || (heartRate > 35 && heartRate < 45)) return "L2";
        return "L3"; // ≥180 或 ≤35
    }

    private String calculateBloodGlucoseLevel(Double bloodGlucose) {
        if (bloodGlucose == 0) return "error";
        if (bloodGlucose >= 4 && bloodGlucose <= 11) return "L0";
        if ((bloodGlucose > 11.1 && bloodGlucose < 19.9) || (bloodGlucose >= 3.5 && bloodGlucose < 3.9)) return "L1";
        if ((bloodGlucose >= 20 && bloodGlucose < 29.9) || (bloodGlucose >= 3 && bloodGlucose < 3.5)) return "L2";
        return "L3"; // ≥30 或 <3
    }

    //封装个人健康数据
    private List<Map<String, Object>> packagePersonalHealthData(List<Map<String, Object>> healthDataList) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map<String, Object> healthData : healthDataList) {
            Map<String, Object> personHealthInfo = new HashMap<>();
            personHealthInfo.put("id", healthData.get("person_id"));
            personHealthInfo.put("gender", healthData.get("gender"));
            personHealthInfo.put("age", healthData.get("age"));
            String dept_id = String.valueOf(healthData.get("dep_id"));
            String address = "";
            if (dept_id.equals("10")) {
                address = "江苏省/徐州市/" + healthData.get("dept_name");
            } else if (dept_id.equals("11")) {
                address = "河南省/郑州市/" + healthData.get("dept_name");
            } else {
                address = "陕西省/西安市/" + healthData.get("dept_name");
            }
            personHealthInfo.put("address", address);
            Map<String, List<String>> levelMap = new HashMap<>();
            levelMap.put("L0", new ArrayList<>());
            levelMap.put("L1", new ArrayList<>());
            levelMap.put("L2", new ArrayList<>());
            levelMap.put("L3", new ArrayList<>());
            levelMap.put("error", new ArrayList<>());
            Double breathRate = Double.parseDouble(String.valueOf(healthData.get("breath_rate")));
            Double systolic = Double.parseDouble(String.valueOf(healthData.get("systolic")));
            Double diastolic = Double.parseDouble(String.valueOf(healthData.get("diastolic")));
            Double bloodOxygen = Double.parseDouble(String.valueOf(healthData.get("blood_oxygen")));
            Double temperature = Double.parseDouble(String.valueOf(healthData.get("temperature")));
            Double heartRate = Double.parseDouble(String.valueOf(healthData.get("heart_rate")));
            Double bloodGlucose = Double.parseDouble(String.valueOf(healthData.get("blood_glucose")));
            String breathRateLevel = calculateBreathRateLevel(breathRate);
            String systolicLevel = calculateSystolicLevel(systolic);
            String diastolicLevel = calculateDiastolicLevel(diastolic);
            String bloodOxygenLevel = calculateBloodOxygenLevel(bloodOxygen);
            String temperatureLevel = calculateTemperatureLevel(temperature);
            String heartRateLevel = calculateHeartRateLevel(heartRate);
            String bloodGlucoseLevel = calculateBloodGlucoseLevel(bloodGlucose);
            levelMap.get(breathRateLevel).add("呼吸率");
            levelMap.get(systolicLevel).add("收缩压");
            levelMap.get(diastolicLevel).add("舒张压");
            levelMap.get(bloodOxygenLevel).add("血氧");
            levelMap.get(temperatureLevel).add("体温");
            levelMap.get(heartRateLevel).add("心率");
            levelMap.get(bloodGlucoseLevel).add("血糖");
            personHealthInfo.putAll(levelMap);
            resultList.add(personHealthInfo);
        }
        return resultList;
    }

    //获取小区全部健康数据
    @Override
    public List<Map<String, Object>> getDataByCommunityAll(String communityName) {
        List<Map<String, Object>> healthDataList = cityMapper.getDataByCommunityAll(communityName);
        List<Map<String, Object>> resultList = packagePersonalHealthData(healthDataList);
        return resultList;
    }

    @Override
    public Map<String, Object> getPersonalHealthData(Integer id) {
       return cityMapper.getPersonalHealthData(id);
    }

    @Override
    public Map<String, Object> getEnvironmentData() {
        return cityMapper.getEnvironmentData();
    }

    @Override
    public Map<String, Object> getEnvironmentDataByCity(String cityName) {
        Map<String, Object> map = new HashMap<>();
        if (cityName.equals("西安市") || cityName.equals("陕西省")) {
            map = cityMapper.getEnvironmentDataByCity(12);
        } else if (cityName.equals("郑州市") || cityName.equals("河南省")) {
            map = cityMapper.getEnvironmentDataByCity(11);
        } else if (cityName.equals("徐州市") || cityName.equals("江苏省"))
            map = cityMapper.getEnvironmentDataByCity(10);
        else
            return null;
        return map;
    }

    @Override
    public Map<String, Object> getCommunityEnvironmentDataByCity(String communityName) {
        return cityMapper.getCommunityEnvironmentDataByCity(communityName);
    }

    //根据城市名查询健康数据
    private List<Map<String, Object>> getHealthDataByCity(Integer id) {
        List<Map<String, Object>> fiftyData;
        if (id == null) {
            fiftyData = cityMapper.getHealthDataRandomFifty();
        } else {
            fiftyData = cityMapper.getHealthDataRandomFiftyByCity(id);
        }
        fiftyData = packagePersonalHealthData(fiftyData);
        Collections.shuffle(fiftyData);
        return fiftyData;
    }

    @Override
    public List<Map<String, Object>> getHealthDataAllByCityName(String name) {
        //1. 如果是空参或者中国
        if (name.isEmpty() || name.equals("中国")) {
            return getHealthDataByCity(null);
        } else {//2. 如果是省市
            if (name.equals("西安市") || name.equals("陕西省")) {
                return getHealthDataByCity(12);
            } else if (name.equals("郑州市") || name.equals("河南省")) {
                return getHealthDataByCity(11);
            } else if (name.equals("徐州市") || name.equals("江苏省"))
                return getHealthDataByCity(10);
            else
                return null;
        }
    }

    @Override
    public List<Map<String, Object>> getHealthDataAll() {
        return getHealthDataByCity(null);
    }

    @Override
    public Map<String, Object> getEnviromentByUserId(Integer id) {
        return cityMapper.getEnviromentByUserId(id);
    }
}
