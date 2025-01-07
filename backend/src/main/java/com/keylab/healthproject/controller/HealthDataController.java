package com.keylab.healthproject.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.keylab.healthproject.common.Indicator;
import com.keylab.healthproject.common.Result;
import com.keylab.healthproject.common.ResultCodeEnum;
import com.keylab.healthproject.dao.HealthData;
import com.keylab.healthproject.service.IHealthDataService;
import com.keylab.healthproject.service.impl.HealthDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author T4mako
 * @date 2024/10/31 13:01
 */
@RestController
@RequestMapping("/hData")
public class HealthDataController {
    @Autowired
    IHealthDataService iHealthDataService;

    @GetMapping("/areaHDataAge")
    public Result getHealthData(@RequestParam long id, @RequestParam String area,@RequestParam String indicator) {
        if(!Indicator.healthIndicators.contains(indicator)){
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        }
        if (area != null && !area.isEmpty()) {
            if(area.equals("community") || area.equals("city") || area.equals("all")){
                return Result.success(iHealthDataService.getAreaHDataAge(id,area,indicator));
            }
        }
        return Result.error(ResultCodeEnum.PARAM_ERROR);
    }
    @GetMapping("/personLatestHData")
    public Result getPersonLatestHData(@RequestParam long id) {
        HealthData healthData = iHealthDataService.personLatestHData(id);
        return Result.success(healthData);
    }

    @GetMapping("/allHealthAndEnvData")
    public Result getAllHealthAndEnvData(@RequestParam long id) {
        List<Map<String, Object>> res = iHealthDataService.getAllHealthAndEnvData(id);
        return Result.success(res);
    }
}
