package com.keylab.healthproject.controller;

import com.keylab.healthproject.common.Result;
import com.keylab.healthproject.common.ResultCodeEnum;
import com.keylab.healthproject.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/city")
public class cityController {
    @Autowired
    private ICityService indexService;

    //查询所有城市对应名称及人数
    @RequestMapping("/getCityNameAndNum")
    public Result getCityNameAndNum(){
        List<Map<String, Object>> lisMap= indexService.getCityNameAndNum();
        return Result.success(lisMap);
    }

    //根据城市名查询对应人数
    @RequestMapping("/getNumByCityName")
    public Result getNumByCityName(@RequestParam String cityName){
        long num= indexService.getNumByCityName(cityName);
        if(num==0)
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        return Result.success(num);
    }

    // 查询所有城市，返回男女数量比例
    @RequestMapping("/getSexCount")
    public Result getSexCount(){
        List<Map<String, Object>> list= indexService.getSexCount();
        return Result.success(list);
    }

    // 根据城市名称，返回该城市的男女数量比例
    @RequestMapping("/getSexCountByCity")
    public Result getSexCountByCity(@RequestParam String cityName){
        List<Map<String, Object>> list= indexService.getSexCountByCity(cityName);
        if(list==null)
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        return Result.success(list);
    }

}
