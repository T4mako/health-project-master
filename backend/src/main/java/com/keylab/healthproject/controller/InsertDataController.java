package com.keylab.healthproject.controller;


import com.keylab.healthproject.common.Result;
import com.keylab.healthproject.dao.*;
import com.keylab.healthproject.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author T4mako
 * @date 2024/10/24 13:22
 * @desc 添加数据测试
 */
@RestController("/")
public class InsertDataController {
    private static final Logger log = LoggerFactory.getLogger(InsertDataController.class);
    @Autowired
    ICommunityService iCommunityService;
    @Autowired
    IEnvValService iEnvValService;
    @Autowired
    IHealthDataService iHealthDataService;
    @Autowired
    IHospitalService iHospitalService;
    @Autowired
    IPersonDataService iPersonDataService;

    // 批量插入 community 数据
    @PostMapping("/community")
    public Result insert2Community(@RequestBody List<Community> list){
        iCommunityService.saveBatch(list);
        return Result.success();
    }
    // 批量插入 envVal 数据
    @PostMapping("/envVal")
    public Result insert2EnvVal(@RequestBody List<EnvVal> list){
        iEnvValService.saveBatch(list);
        return Result.success();
    }
    // 批量插入 healthData 数据
    @PostMapping("/healthData")
    public Result insert2HealthData(@RequestBody List<HealthData> list){
        iHealthDataService.saveBatch(list);
        return Result.success();
    }
    // 批量插入 hospital 数据
    @PostMapping("/hospital")
    public Result insert2Hospital(@RequestBody List<Hospital> list){
        iHospitalService.saveBatch(list);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result test2(@PathVariable Integer id){
        PersonData byId = iPersonDataService.getById(id);
        return Result.success(byId);
    }

}
