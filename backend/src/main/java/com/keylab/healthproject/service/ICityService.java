package com.keylab.healthproject.service;

import java.util.List;
import java.util.Map;

public interface ICityService {
    // 获取城市名及人口数量
    List<Map<String, Object>> getCityNameAndNum();

    // 获取指定城市的人口数量
    long getNumByCityName(String cityName);

    // 获取所有城市男女人口数量
    List<Map<String, Object>> getSexCount();

    // 获取城市的男女人口数量
    List<Map<String, Object>> getSexCountByCity(String cityName);


}
