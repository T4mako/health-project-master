package com.keylab.healthproject.controller;

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

/**
 * @author T4mako
 * @date 2024/10/31 13:01
 */
@RestController
@RequestMapping("/")
public class HealthDataController {
    @Autowired
    IHealthDataService iHealthDataService;

    @GetMapping("/hData")
    public Result getHealthData(@RequestParam long id,@RequestParam int flag, @RequestParam String indicator) {
        if(!Indicator.healthIndicators.contains(indicator)){
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        }
        return Result.success(iHealthDataService.getDataByArea(id,flag,indicator));
    }
}
