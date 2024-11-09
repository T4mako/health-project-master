package com.keylab.healthproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keylab.healthproject.common.Result;
import com.keylab.healthproject.dao.HealthData;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author T4mako
 * @since 2024-10-24
 */
public interface IHealthDataService extends IService<HealthData> {

    List<HealthData> dayHData(long id);

    List<HealthData> dateHData(long id, String date);

    List<Map<String, Object>> getDataByArea(long id, int flag, String indicator);

    List<String> getThresholdWarnings(long id);
}
