package com.keylab.healthproject.service.impl;

import com.keylab.healthproject.mapper.CityMapper;
import com.keylab.healthproject.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if(cityName.equals("西安市") ||cityName.equals("陕西省")){
            return cityMapper.getNumByCityId(12);
        } else if (cityName.equals("郑州市")||cityName.equals("河南省")) {
            return cityMapper.getNumByCityId(11);
        }else if (cityName.equals("徐州市")||cityName.equals("江苏省"))
            return cityMapper.getNumByCityId(10);
        else
            return 0;

    }

    @Override
    public List<Map<String, Object>> getSexCount() {
        List<Map<String, Object>> list= cityMapper.getSexCount();
        Map<String, Object> data = list.get(0);
        // 确保键存在，并直接使用 BigDecimal
        BigDecimal maleCountBD = (BigDecimal) data.get("男");
        BigDecimal femaleCountBD = (BigDecimal) data.get("女");
        Integer maleCount = maleCountBD.intValue();
        Integer femaleCount = femaleCountBD.intValue();
        // 计算比例
        Double ratio = 0.0;
        if (femaleCount != 0) {
            ratio = maleCount.doubleValue() / femaleCount.doubleValue();
        }
        // 将比例添加到结果中
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
        List<Map<String, Object>> list= new ArrayList();
        if(cityName.equals("西安市") ||cityName.equals("陕西省")){
            list= cityMapper.getSexCountByCity(12);
        } else if (cityName.equals("郑州市")||cityName.equals("河南省")) {
            list= cityMapper.getSexCountByCity(11);
        }else if (cityName.equals("徐州市")||cityName.equals("江苏省"))
            list= cityMapper.getSexCountByCity(10);
        else
            return null;
        // 将男女比例作为键值对加入list
        // 将男女比例作为键值对加入 list
        Map<String, Object> data = list.get(0);


        // 确保键存在，并直接使用 BigDecimal
        BigDecimal maleCountBD = (BigDecimal) data.get("男");
        BigDecimal femaleCountBD = (BigDecimal) data.get("女");

        Integer maleCount = maleCountBD.intValue();
        Integer femaleCount = femaleCountBD.intValue();
        // 计算比例
        Double ratio = 0.0;
        if (femaleCount != 0) {
            ratio = maleCount.doubleValue() / femaleCount.doubleValue();
        }
        // 将比例添加到结果中
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
        List<Map<String, Object>> list= new ArrayList();
        if(cityName.equals("西安市") ||cityName.equals("陕西省")){
            list= cityMapper.getHealthStatusByCity(12);
        } else if (cityName.equals("郑州市")||cityName.equals("河南省")) {
            list= cityMapper.getHealthStatusByCity(11);
        }else if (cityName.equals("徐州市")||cityName.equals("江苏省"))
            list= cityMapper.getHealthStatusByCity(10);
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
        List<Map<String, Object>> list= new ArrayList();
        if(cityName.equals("西安市") ||cityName.equals("陕西省")){
            list= cityMapper.getHealthLevelByCity(12);
        } else if (cityName.equals("郑州市")||cityName.equals("河南省")) {
            list= cityMapper.getHealthLevelByCity(11);
        }else if (cityName.equals("徐州市")||cityName.equals("江苏省"))
            list= cityMapper.getHealthLevelByCity(10);
        else
            return null;
        return list;
    }
}
