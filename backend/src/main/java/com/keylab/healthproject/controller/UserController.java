package com.keylab.healthproject.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.keylab.healthproject.common.Result;
import com.keylab.healthproject.common.ResultCodeEnum;
import com.keylab.healthproject.dao.Community;
import com.keylab.healthproject.dao.Hospital;
import com.keylab.healthproject.dao.PersonData;
import com.keylab.healthproject.service.ICommunityService;
import com.keylab.healthproject.service.IHealthDataService;
import com.keylab.healthproject.service.IHospitalService;
import com.keylab.healthproject.service.IPersonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author T4mako
 * @date 2024/10/31 9:21
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    IPersonDataService iPersonDataService;

    @Autowired
    IHealthDataService iHealthDataService;

    @Autowired
    ICommunityService iCommunityService;

    @Autowired
    IHospitalService iHospitalService;

    @GetMapping("/info")
    public Result userInfo(@RequestParam long id) {
        PersonData pd = iPersonDataService.getById(id);
        if (pd != null) {
            // 获取城市名
            Community community = iCommunityService.getById(pd.getDeptId());
            long depId = community.getDepId();
            LambdaQueryWrapper<Hospital> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Hospital::getDeptId, depId);
            Hospital hospital = iHospitalService.getOne(queryWrapper);
            String city = hospital.getName().substring(0,2);
            Map<String, Object> data = new HashMap<>();
            data.put("pd", pd);
            data.put("city", city);
            return Result.success(data);
        }
        return Result.error(ResultCodeEnum.PARAM_ERROR);
    }

    @GetMapping("/dayHData")
    public Result dayHData(@RequestParam long id) {
        return Result.success(iHealthDataService.dayHData(id));
    }

    @GetMapping("/dateHData")
    public Result dateHData(@RequestParam long id, @RequestParam String date) {
        if(date.equals("week") || date.equals("month") || date.equals("year") || date.equals("all")) {
            return Result.success(iHealthDataService.dateHData(id,date));
        }else {
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        }
    }


}
