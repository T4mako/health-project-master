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

    // 返回各个省所有社区健康情况累加和
    List<Map<String, Object>> getHealthStatus();

    // 返回指定城市的健康情况
    List<Map<String, Object>> getHealthStatusByCity(String cityName);

    // 返回健康等级
    List<Map<String, Object>> getHealthLevel();

    // 返回指定城市的健康等级
    List<Map<String, Object>> getHealthLevelByCity(String cityName);

    // 返回指定社区的健康数据
    Map<String, Object> getHealthDataByCommunity(String communityName);

    // 返回指定社区全员的数据
    List<Map<String, Object>> getDataByCommunityAll(String communityName);

    // 返回个人健康数据
    Map<String, Object> getPersonalHealthData(Integer id);
}
