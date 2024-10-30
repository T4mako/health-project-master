package com.keylab.healthproject.service;

import java.util.List;
import java.util.Map;

public interface IIndexService {
    // 获取城市名及人口数量
    List<Map<String, Object>> getCityNameAndNum();
}
